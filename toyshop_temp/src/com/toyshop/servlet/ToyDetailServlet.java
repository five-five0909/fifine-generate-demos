package com.toyshop.servlet;

import com.toyshop.bean.Toy;
import com.toyshop.bean.User; // 需要User来检查登录状态
import com.toyshop.dao.ToyDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// 玩具详情Servlet
@WebServlet("/toyDetail")
public class ToyDetailServlet extends HttpServlet {
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

        HttpSession session = request.getSession(false); // 不创建新session

        // 1. 检查用户是否登录
        if (session == null || session.getAttribute("user") == null) {
            // 用户未登录，重定向到登录页面，并可以传递原始请求的URL以便登录后返回
            // response.sendRedirect(request.getContextPath() + "/login.jsp?redirectUrl=" + request.getRequestURI() + "?" + request.getQueryString());
            // 为了简化，直接重定向到登录页
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 2. 获取玩具ID参数
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少玩具ID参数。");
            return;
        }

        try {
            int toyId = Integer.parseInt(idStr);
            Toy toy = toyDao.findById(toyId);

            if (toy != null) {
                request.setAttribute("toy", toy);
                request.setAttribute("pageTitle", "玩具详情 - " + toy.getToyName());
                request.getRequestDispatcher("toy_detail.jsp").forward(request, response);
            } else {
                // 如果没有找到玩具，可以显示一个错误消息或重定向
                request.setAttribute("errorMessage", "未找到指定的玩具。");
                request.setAttribute("pageTitle", "未找到玩具");
                // 可以创建一个通用的错误页面，或者重定向回列表页
                request.getRequestDispatcher("toy_list.jsp").forward(request, response); // 或者一个专门的error.jsp
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的玩具ID格式。");
        }
    }
}
