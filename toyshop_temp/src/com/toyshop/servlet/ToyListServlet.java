package com.toyshop.servlet;

import com.toyshop.bean.Toy;
import com.toyshop.dao.ToyDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// 玩具列表展示 (首页/浏览) Servlet
@WebServlet({"/index", "/toyList"}) // 可以映射到 /index 和 /toyList
public class ToyListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToyDao toyDao;
    private static final int DEFAULT_PAGE_SIZE = 8; // 每页显示的玩具数量

    @Override
    public void init() throws ServletException {
        toyDao = new ToyDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        int pageNum = 1; // 默认页码
        String pageNumStr = request.getParameter("pageNum");
        if (pageNumStr != null && !pageNumStr.isEmpty()) {
            try {
                pageNum = Integer.parseInt(pageNumStr);
                if (pageNum < 1) {
                    pageNum = 1; // 页码不能小于1
                }
            } catch (NumberFormatException e) {
                // 参数不是有效数字，使用默认值
                System.err.println("无效的页码参数: " + pageNumStr);
            }
        }

        List<Toy> toyList = toyDao.findAll(pageNum, DEFAULT_PAGE_SIZE);
        int totalToys = toyDao.getTotalToyCount();
        int totalPages = (int) Math.ceil((double) totalToys / DEFAULT_PAGE_SIZE);

        request.setAttribute("toyList", toyList);
        request.setAttribute("currentPage", pageNum);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalToys", totalToys); // 也传递总玩具数，可能有用

        // 检查是否是搜索结果的请求（通过是否有 searchResults 属性判断）
        // 如果是搜索结果，toy_list.jsp 可能需要显示不同的标题或信息
        if (request.getAttribute("isSearchResult") == null) {
             request.setAttribute("pageTitle", "欢迎来到儿童玩具售卖系统 - 浏览玩具");
        }


        request.getRequestDispatcher("toy_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST请求通常不用于列表展示，但也转发到doGet处理
        doGet(request, response);
    }
}
