package com.toyshop.servlet;

import com.toyshop.bean.Toy;
import com.toyshop.dao.ToyDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class ToySearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToyDao toyDao;

    @Override
    public void init() throws ServletException {
        toyDao = new ToyDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String keyword = request.getParameter("keyword");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        Double minPrice = null;
        Double maxPrice = null;

        try {
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceStr);
            }
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceStr);
                if (maxPrice <= 0 && (minPrice == null || minPrice < maxPrice)) { // If maxPrice is 0 or negative, treat as no upper limit unless minPrice is higher
                    maxPrice = null;
                }
            }
        } catch (NumberFormatException e) {
            // Log error or handle gracefully, e.g., by ignoring invalid price
            System.err.println("Invalid price format in search: " + e.getMessage());
            // Potentially set an error message for the user
            request.setAttribute("searchErrorMessage", "Invalid price format entered.");
        }

        // Keep search parameters to repopulate the form
        request.setAttribute("keyword", keyword);
        request.setAttribute("minPrice", minPriceStr); // Keep original string for form
        request.setAttribute("maxPrice", maxPriceStr); // Keep original string for form


        List<Toy> toyList = toyDao.search(keyword, minPrice, maxPrice);

        request.setAttribute("toyList", toyList);
        request.setAttribute("isSearchResult", true); // Flag to distinguish from general listing
        if (toyList.isEmpty()) {
            request.setAttribute("searchMessage", "No toys found matching your criteria.");
        }


        // Forward to toy_list.jsp to display results.
        // toy_list.jsp will need to be adapted to handle search results (e.g., no pagination for search results in this simple version)
        request.getRequestDispatcher("toy_list.jsp").forward(request, response);
    }
}
