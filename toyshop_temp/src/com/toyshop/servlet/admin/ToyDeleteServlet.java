package com.toyshop.servlet.admin;

import com.toyshop.bean.Toy; // 可能需要Toy来获取图片路径以删除文件
import com.toyshop.bean.User;
import com.toyshop.dao.ToyDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// 删除玩具Servlet
@WebServlet("/admin/deleteToy")
public class ToyDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToyDao toyDao;

    @Override
    public void init() throws ServletException {
        toyDao = new ToyDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/manage?error=missingId");
            return;
        }

        try {
            int toyId = Integer.parseInt(idStr);

            // （可选但推荐）删除玩具前，先获取玩具信息，以便删除关联的图片文件
            Toy toyToDelete = toyDao.findById(toyId);
            String imageUrlToDelete = null;
            if (toyToDelete != null) {
                imageUrlToDelete = toyToDelete.getImageUrl();
            }

            boolean success = toyDao.deleteToy(toyId);

            if (success) {
                // 如果数据库删除成功，并且获取到了图片URL，则尝试删除图片文件
                if (imageUrlToDelete != null && !imageUrlToDelete.isEmpty()) {
                    try {
                        String uploadPath = getServletContext().getRealPath(""); // 基本路径
                        // imageUrlToDelete 存储的是 "assets/images/filename.jpg" 这样的相对路径
                        // 所以需要拼接
                        File imageFile = new File(uploadPath + File.separator + imageUrlToDelete);
                        if (imageFile.exists()) {
                            Files.deleteIfExists(imageFile.toPath());
                            System.out.println("成功删除了玩具图片: " + imageUrlToDelete);
                        } else {
                             System.out.println("玩具图片未找到，无法删除: " + imageUrlToDelete);
                        }
                    } catch (IOException e) {
                        System.err.println("删除玩具图片文件失败 ("+ imageUrlToDelete +"): " + e.getMessage());
                        // 即使图片删除失败，数据库记录也已删除，所以仍然认为是部分成功
                        // 可以在日志中记录此错误
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/manage?deleteSuccess=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/manage?deleteError=true&toyId=" + toyId);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/manage?error=invalidIdFormat");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/manage?error=exceptionOnDelete");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 通常删除操作通过GET（例如链接点击），但也允许POST以防万一
        doGet(request, response);
    }
}
