package com.toyshop.servlet;

import com.toyshop.bean.Toy;
import com.toyshop.dao.ToyDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/index") // Also serves as the entry point / home page
public class ToyListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToyDao toyDao;

    @Override
    public void init() throws ServletException {
        toyDao = new ToyDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNum = 1;
        int pageSize = 8; // Number of toys per page, can be configurable

        String pageNumStr = request.getParameter("pageNum");
        if (pageNumStr != null && !pageNumStr.isEmpty()) {
            try {
                pageNum = Integer.parseInt(pageNumStr);
                if (pageNum < 1) {
                    pageNum = 1;
                }
            } catch (NumberFormatException e) {
                // Log error or default to page 1
                System.err.println("Invalid page number format: " + pageNumStr);
                pageNum = 1;
            }
        }

        List<Toy> toyList = toyDao.findAll(pageNum, pageSize);
        int totalToyCount = toyDao.getTotalToyCount();
        int totalPages = (int) Math.ceil((double) totalToyCount / pageSize);

        request.setAttribute("toyList", toyList);
        request.setAttribute("currentPage", pageNum);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalToyCount", totalToyCount); // For display if needed

        request.getRequestDispatcher("toy_list.jsp").forward(request, response);
    }
}
