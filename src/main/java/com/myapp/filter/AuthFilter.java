package com.myapp.filter;

import com.myapp.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        // Allow public pages/resources
        boolean isPublic =
                uri.equals(ctx + "/login") ||
                uri.equals(ctx + "/logout") ||
                uri.equals(ctx + "/forgot-password") ||
                uri.equals(ctx + "/reset-password") ||
                uri.startsWith(ctx + "/assets/") ||
                uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg") ||
                uri.endsWith(".jpeg") || uri.endsWith(".gif") || uri.endsWith(".svg");

        if (isPublic) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(ctx + "/login");
            return;
        }

        String role = user.getRole(); // ADAPT: ensure your User model has getRole()

        // Protect admin routes
        if (uri.startsWith(ctx + "/admin") && !"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        // Protect academic routes
        if (uri.startsWith(ctx + "/academic") && !"ACADEMIC".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        chain.doFilter(req, res);
    }
}
