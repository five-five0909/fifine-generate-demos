package com.toyshop.servlet.admin;

import com.toyshop.bean.Toy;
import com.toyshop.dao.ToyDao;
import com.toyshop.bean.User; // 用于权限检查

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// 管理员后台玩具列表Servlet
@WebServlet("/admin/manage")
public class AdminToyListServlet extends HttpServlet {
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

        // 权限检查：确保是管理员访问
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp"); // 未登录，重定向到登录页
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getRole() != 2) { // 1是普通用户, 2是管理员
            // 非管理员用户，可以重定向到首页或错误提示页
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        // 获取所有玩具列表 (后台管理通常不分页或使用不同的分页策略)
        // 按题目要求，"可不分页以简化后台管理"
        // 如果需要分页，可以复用ToyListServlet的逻辑或单独实现
        int pageNum = 1;
        int pageSize = 1000; // 或者一个足够大的数来获取所有，或者去掉分页逻辑
        String pageNumStr = request.getParameter("pageNum");
         if (pageNumStr != null && !pageNumStr.isEmpty()) {
            try {
                pageNum = Integer.parseInt(pageNumStr);
                if (pageNum < 1) pageNum = 1;
            } catch (NumberFormatException e) {
                pageNum = 1;
            }
        }
        // 对于后台，也可以考虑是否真的需要分页，如果玩具数量巨大则需要
        // 这里暂时简单获取所有，或者一个较大的固定数量
        // List<Toy> toyList = toyDao.findAll(1, Integer.MAX_VALUE); // 获取所有，可能性能问题
        // 或者获取固定数量的第一页
        List<Toy> toyList = toyDao.findAll(pageNum, pageSize); // 使用分页
        int totalToys = toyDao.getTotalToyCount();
        int totalPages = (int) Math.ceil((double) totalToys / pageSize);


        request.setAttribute("toyList", toyList);
        request.setAttribute("currentPage", pageNum);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageTitle", "后台玩具管理");
        request.getRequestDispatcher("/admin/toy_manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // POST请求也由doGet处理
    }
}
