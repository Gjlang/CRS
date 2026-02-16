package com.crs.servlet.auth;

import com.crs.service.PasswordResetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final PasswordResetService passwordResetService = new PasswordResetService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("token", request.getParameter("token"));
        request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String pass1 = request.getParameter("password");
        String pass2 = request.getParameter("confirm");

        if (pass1 == null || !pass1.equals(pass2)) {
            request.setAttribute("error", "Passwords do not match.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
            return;
        }

        boolean ok = passwordResetService.resetPassword(token, pass1);
        if (!ok) {
            request.setAttribute("error", "Invalid or expired token.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/login?reset=1");
    }
}
