package com.toyshop.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class UserLogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get session if exists, don't create new one

        if (session != null) {
            session.removeAttribute("user"); // Remove user attribute
            session.invalidate(); // Invalidate the session
        }

        // Redirect to login page
        // Optionally, add a message like "You have been logged out successfully."
        // request.getSession().setAttribute("successMessage", "You have been logged out successfully."); // if login.jsp can show it
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Typically logout is handled via GET, but can redirect if POST is used.
        doGet(request, response);
    }
}
