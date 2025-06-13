package com.toyshop.servlet;

import com.toyshop.bean.Toy;
import com.toyshop.bean.User;
import com.toyshop.dao.ToyDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/toyDetail")
public class ToyDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToyDao toyDao;

    @Override
    public void init() throws ServletException {
        toyDao = new ToyDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Don't create if no session

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            // User not logged in, redirect to login page
            // Optionally, save the intended destination to redirect back after login
            // session.setAttribute("redirectAfterLogin", request.getRequestURI() + "?" + request.getQueryString());
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            // No ID provided, redirect to home or error page
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        try {
            int toyId = Integer.parseInt(idStr);
            Toy toy = toyDao.findById(toyId);

            if (toy != null) {
                request.setAttribute("toy", toy);
                request.getRequestDispatcher("toy_detail.jsp").forward(request, response);
            } else {
                // Toy not found, perhaps show an error message or redirect
                request.setAttribute("errorMessage", "Toy not found.");
                // Forward to a generic error page or back to list, here forwarding to list
                response.sendRedirect(request.getContextPath() + "/index?error=ToyNotFound");
            }
        } catch (NumberFormatException e) {
            // Invalid ID format
            System.err.println("Invalid toy ID format: " + idStr + " - " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/index?error=InvalidToyId");
        }
    }
}
