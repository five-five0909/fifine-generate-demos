package com.toyshop.servlet;

import com.toyshop.bean.Toy;
import com.toyshop.dao.ToyDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// 玩具搜索Servlet
@WebServlet("/search")
public class ToySearchServlet extends HttpServlet {
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

        // 1. 检查用户是否登录
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 2. 获取搜索参数
        String keyword = request.getParameter("keyword");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        double minPrice = 0;
        double maxPrice = 0;

        try {
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceStr);
            }
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceStr);
            }
        } catch (NumberFormatException e) {
            // 如果价格格式错误，可以设置一个错误消息，或者直接忽略这个筛选条件
            request.setAttribute("errorMessage", "价格格式无效，将忽略价格筛选。");
            // 或者重定向回搜索页并提示错误
            // response.sendRedirect(request.getContextPath() + "/toyList?searchError=priceFormat");
            // return;
        }

        // 进行校验，例如maxPrice不能小于minPrice (如果都输入了的话)
        if (maxPrice > 0 && minPrice > 0 && maxPrice < minPrice) {
             request.setAttribute("warningMessage", "最高价格不能低于最低价格，价格筛选可能无效。");
             // 可以选择重置价格或如何处理
        }


        List<Toy> searchResults = toyDao.search(keyword, minPrice, maxPrice);

        request.setAttribute("toyList", searchResults); // toy_list.jsp 使用 "toyList" 属性
        request.setAttribute("isSearchResult", true); // 标记这是搜索结果
        request.setAttribute("searchKeyword", keyword); // 回显搜索关键字
        request.setAttribute("searchMinPrice", minPriceStr); // 回显最低价
        request.setAttribute("searchMaxPrice", maxPriceStr); // 回显最高价

        if (searchResults.isEmpty()){
            request.setAttribute("pageTitle", "玩具搜索结果 - 未找到匹配项");
            request.setAttribute("noResultsMessage", "抱歉，没有找到符合条件的玩具。");
        } else {
            request.setAttribute("pageTitle", "玩具搜索结果");
        }


        // 转发到 toy_list.jsp 显示结果 (toy_list.jsp 需要能处理搜索结果的展示)
        // ToyListServlet 中的分页逻辑在这里不直接适用，除非search也实现分页
        // 为了简化，当前search不分页，直接展示所有结果
        // 如果需要分页，ToyDao.search方法和此Servlet都需要相应修改
        request.setAttribute("currentPage", 1); // 搜索结果通常显示为一页，或需要独立分页
        request.setAttribute("totalPages", 1); // 假设搜索结果不分页或只有一页

        request.getRequestDispatcher("toy_list.jsp").forward(request, response);
    }
}
