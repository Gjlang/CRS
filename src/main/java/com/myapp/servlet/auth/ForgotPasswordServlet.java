package com.crs.servlet.auth;

import com.crs.service.PasswordResetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final PasswordResetService passwordResetService = new PasswordResetService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/forgot_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        String base = request.getRequestURL().toString()
                .replace("/forgot-password", "/reset-password");

        passwordResetService.requestReset(email, base);

        request.setAttribute("message", "If the email exists, a reset link has been sent.");
        request.getRequestDispatcher("/forgot_password.jsp").forward(request, response);
    }
}
