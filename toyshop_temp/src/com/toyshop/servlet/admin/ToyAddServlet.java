package com.toyshop.servlet.admin;

import com.toyshop.bean.Toy;
import com.toyshop.bean.User;
import com.toyshop.dao.ToyDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// 添加玩具Servlet (处理文件上传)
@WebServlet("/admin/addToy")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class ToyAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToyDao toyDao;
    private static final String UPLOAD_DIRECTORY = "assets/images"; // 相对于WebContent的路径

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

        // 权限检查：确保是管理员
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
            String toyName = request.getParameter("toyName");
            String brand = request.getParameter("brand");
            String category = request.getParameter("category");
            String priceStr = request.getParameter("price");
            String description = request.getParameter("description");
            Part filePart = request.getPart("imageFile"); // "imageFile" 是 JSP 中 <input type="file"> 的 name

            // 数据校验 (简单示例，可以做得更完善)
            if (toyName == null || toyName.trim().isEmpty() ||
                brand == null || brand.trim().isEmpty() ||
                category == null || category.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                filePart == null || filePart.getSize() == 0) {
                request.setAttribute("errorMessage", "所有必填字段（包括图片）都不能为空！");
                request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
                return;
            }

            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                     request.setAttribute("errorMessage", "价格不能为负数！");
                     request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
                     return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "价格格式无效！");
                request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
                return;
            }


            // 处理文件上传
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            // 为避免文件名冲突，可以生成唯一文件名
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            // 获取应用的真实部署路径，然后拼接上传目录
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // 创建目录，如果它不存在
            }

            String imageUrl = null; // 存储到数据库的相对路径
            if (fileName != null && !fileName.isEmpty()) {
                try (InputStream fileContent = filePart.getInputStream()) {
                    File file = new File(uploadPath + File.separator + uniqueFileName);
                    Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    imageUrl = UPLOAD_DIRECTORY + "/" + uniqueFileName; // 保存相对路径
                } catch (IOException e) {
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "图片上传失败：" + e.getMessage());
                    request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
                    return;
                }
            } else {
                 request.setAttribute("errorMessage", "必须上传玩具图片！");
                 request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
                 return;
            }


            Toy toy = new Toy();
            toy.setToyName(toyName);
            toy.setBrand(brand);
            toy.setCategory(category);
            toy.setPrice(price);
            toy.setDescription(description);
            toy.setImageUrl(imageUrl); // 存储相对路径

            boolean success = toyDao.addToy(toy);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/manage?addSuccess=true"); // 重定向到管理页并提示成功
            } else {
                request.setAttribute("errorMessage", "添加玩具失败，请检查数据或联系管理员。");
                // 如果添加失败，可能需要考虑删除已上传的图片
                if (imageUrl != null) {
                    try {
                        Files.deleteIfExists(Paths.get(uploadPath + File.separator + uniqueFileName));
                    } catch (IOException ex) {
                        System.err.println("删除未成功添加的玩具图片失败: " + ex.getMessage());
                    }
                }
                request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "处理请求时发生错误：" + e.getMessage());
            request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 管理员直接访问添加页面
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || ((User)session.getAttribute("user")).getRole() != 2 ) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.setAttribute("pageTitle", "添加新玩具");
        request.getRequestDispatcher("/admin/add_toy.jsp").forward(request, response);
    }
}
