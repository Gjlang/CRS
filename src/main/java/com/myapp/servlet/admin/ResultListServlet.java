package com.myapp.servlet.admin;

import com.myapp.dao.StudentResultDAO;
import com.myapp.model.StudentResult;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/results")
public class ResultListServlet extends HttpServlet {
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

        String studentId = req.getParameter("student_id");

        List<StudentResult> results;
        if (studentId != null && !studentId.trim().isEmpty()) {
            results = resultDAO.findByStudentId(studentId.trim());
            req.setAttribute("filterStudentId", studentId.trim());
        } else {
            results = resultDAO.findAll();
        }

        req.setAttribute("results", results);
        req.getRequestDispatcher("/admin/results/list.jsp").forward(req, resp);
    }
}
