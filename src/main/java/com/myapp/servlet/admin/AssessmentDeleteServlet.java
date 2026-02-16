package com.myapp.servlet.admin;

import com.myapp.dao.AssessmentDAO;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/assessments/delete")
public class AssessmentDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final AssessmentDAO assessmentDAO = new AssessmentDAO();

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
        String courseCode = req.getParameter("course_code");

        if (idStr == null || idStr.trim().isEmpty() || courseCode == null || courseCode.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        try {
            int id = Integer.parseInt(idStr.trim());
            assessmentDAO.delete(id);
        } catch (NumberFormatException ignored) {}

        resp.sendRedirect(req.getContextPath() + "/admin/assessments?course_code=" + courseCode.trim());
    }
}
