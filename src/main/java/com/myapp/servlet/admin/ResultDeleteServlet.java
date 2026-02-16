package com.myapp.servlet.admin;

import com.myapp.dao.StudentResultDAO;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/results/delete")
public class ResultDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final StudentResultDAO resultDAO = new StudentResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"ADMIN".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/results");
            return;
        }

        try {
            int id = Integer.parseInt(idStr.trim());
            resultDAO.delete(id);
        } catch (NumberFormatException ignored) {}

        resp.sendRedirect(req.getContextPath() + "/admin/results");
    }
}
