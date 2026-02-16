package com.myapp.servlet.academic;

import com.myapp.dao.StudentResultDAO;
import com.myapp.model.StudentResult;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/academic/results")
public class AcademicResultServlet extends HttpServlet {
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

        // Academic only (ADMIN should not use this page)
        if (!"ACADEMIC".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String studentId = req.getParameter("student_id");
        if (studentId == null || studentId.trim().isEmpty()) {
            req.setAttribute("error", "Please enter student_id to view results.");
            req.getRequestDispatcher("/academic/results.jsp").forward(req, resp);
            return;
        }

        studentId = studentId.trim();

        List<StudentResult> results = resultDAO.findByStudentId(studentId);

        int failCount = 0;
        if (results != null) {
            for (StudentResult r : results) {
                if (!r.isPassed()) failCount++;
            }
        }

        req.setAttribute("studentId", studentId);
        req.setAttribute("results", results);
        req.setAttribute("failCount", failCount);

        req.getRequestDispatcher("/academic/results.jsp").forward(req, resp);
    }
}
