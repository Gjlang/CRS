package com.myapp.servlet.student;

import com.myapp.dao.StudentDAO;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/students/deactivate")
public class StudentDeactivateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final StudentDAO studentDAO = new StudentDAO();

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
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "ADMIN only");
            return;
        }

        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/students");
            return;
        }

        studentDAO.deactivate(id.trim());

        // always return back to list
        resp.sendRedirect(req.getContextPath() + "/admin/students");
    }
}
