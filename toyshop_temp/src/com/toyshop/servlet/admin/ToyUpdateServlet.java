package com.toyshop.servlet.admin;

import com.toyshop.bean.Toy;
import com.toyshop.bean.User;
import com.toyshop.dao.ToyDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // 注意：如果表单包含文件上传，也需要 @MultipartConfig
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// 如果需要处理图片更新，则需要 Part 和文件操作相关的 import
// import jakarta.servlet.http.Part;
// import java.io.File;
// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.UUID;
import java.io.IOException;
import java.math.BigDecimal;


// 更新玩具Servlet
@WebServlet("/admin/updateToy")
// 如果 edit_toy.jsp 的表单也包含 enctype="multipart/form-data" 以便更新图片, 则需要下一行注解
// @MultipartConfig
public class ToyUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToyDao toyDao;
    // private static final String UPLOAD_DIRECTORY = "assets/images"; // 如果处理图片更新

    @Override
    public void init() throws ServletException {
        toyDao = new ToyDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        // 权限检查
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getRole() != 2) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        try {
            // 获取表单字段
            String toyIdStr = request.getParameter("toyId");
            String toyName = request.getParameter("toyName");
            String brand = request.getParameter("brand");
            String category = request.getParameter("category");
            String priceStr = request.getParameter("price");
            String imageUrl = request.getParameter("imageUrl"); // 直接获取图片URL文本
            String description = request.getParameter("description");

            // 数据校验
            if (toyIdStr == null || toyIdStr.trim().isEmpty() ||
                toyName == null || toyName.trim().isEmpty() ||
                brand == null || brand.trim().isEmpty() ||
                category == null || category.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                imageUrl == null || imageUrl.trim().isEmpty()) { // imageUrl 也设为必填
                request.setAttribute("errorMessage", "所有必填字段都不能为空！");
                // 需要将toyId传回，以便edit_toy.jsp能正确加载
                request.setAttribute("toyIdForEdit", toyIdStr); // 用于错误时返回编辑页
                request.getRequestDispatcher("/admin/edit_toy.jsp").forward(request, response);
                return;
            }

            int toyId;
            BigDecimal price;
            try {
                toyId = Integer.parseInt(toyIdStr);
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                     request.setAttribute("errorMessage", "价格不能为负数！");
                     request.setAttribute("toyIdForEdit", toyIdStr);
                     request.getRequestDispatcher("/admin/edit_toy.jsp?id=" + toyIdStr).forward(request, response); // 转发回编辑页
                     return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "玩具ID或价格格式无效！");
                // 如果toyIdStr本身就无效，可能需要重定向到列表页
                response.sendRedirect(request.getContextPath() + "/admin/manage?error=invalidIdFormat");
                return;
            }

            // **图片更新逻辑 (当前为不直接处理上传，仅更新URL)**
            // 如果需要处理图片文件更新，这里的逻辑会复杂得多：
            // 1. 检查是否有新的 Part filePart = request.getPart("newImageFile");
            // 2. 如果有新文件上传:
            //    a. 保存新文件 (类似ToyAddServlet的逻辑)
            //    b. 更新imageUrl为新文件的路径
            //    c. (可选) 删除旧的图片文件 (需要先从数据库或隐藏字段获取旧的imageUrl)
            // 3. 如果没有新文件上传，则保留原imageUrl (从隐藏字段或数据库获取，或直接使用表单提交的imageUrl)
            // 当前假设 imageUrl 是表单中一个可编辑的文本字段。

            Toy toy = new Toy();
            toy.setToyId(toyId);
            toy.setToyName(toyName);
            toy.setBrand(brand);
            toy.setCategory(category);
            toy.setPrice(price);
            toy.setImageUrl(imageUrl); // 使用表单提交的 image URL
            toy.setDescription(description);

            boolean success = toyDao.updateToy(toy);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/manage?updateSuccess=true");
            } else {
                request.setAttribute("errorMessage", "更新玩具信息失败。");
                request.setAttribute("toy", toy); // 将toy对象传回以便JSP回填
                request.getRequestDispatcher("/admin/edit_toy.jsp?id=" + toyId).forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时，尝试获取toyId以便正确重定向回编辑页
            String toyIdParam = request.getParameter("toyId");
            request.setAttribute("errorMessage", "处理请求时发生错误：" + e.getMessage());
            if (toyIdParam != null && !toyIdParam.isEmpty()) {
                 request.getRequestDispatcher("/admin/edit_toy.jsp?id=" + toyIdParam).forward(request, response);
            } else {
                // 如果toyId都拿不到，只能去列表页了
                 response.sendRedirect(request.getContextPath() + "/admin/manage?error=exception");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET请求到此Servlet通常是错误的，更新操作应由POST发起。
        // 可以重定向到管理页面或显示错误。
        // 不过，edit_toy.jsp页面本身是通过GET请求加载的，它会直接请求JSP或通过一个专门的Servlet加载数据。
        // 此Servlet主要负责处理POST更新。
        // 为防止直接GET访问造成混乱，可以做权限检查后重定向。
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || ((User)session.getAttribute("user")).getRole() != 2 ) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // 如果管理员直接GET访问此URL，重定向到玩具管理首页
        response.sendRedirect(request.getContextPath() + "/admin/manage");
    }
}
