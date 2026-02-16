package com.myapp.servlet.admin;

import com.myapp.dao.StudentResultDAO;
import com.myapp.model.StudentResult;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/results/create")
public class ResultCreateServlet extends HttpServlet {
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

        req.getRequestDispatcher("/admin/results/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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

        String enrolStr = req.getParameter("enrolmentId");
        String assessStr = req.getParameter("assessmentId");
        String grade = req.getParameter("grade");
        String gpStr = req.getParameter("gradePoint");
        String passedStr = req.getParameter("passed");

        if (isBlank(enrolStr) || isBlank(assessStr) || isBlank(grade) || isBlank(gpStr) || isBlank(passedStr)) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/admin/results/create.jsp").forward(req, resp);
            return;
        }

        int enrolmentId, assessmentId;
        double gradePoint;
        boolean passed;

        try {
            enrolmentId = Integer.parseInt(enrolStr);
            assessmentId = Integer.parseInt(assessStr);
            gradePoint = Double.parseDouble(gpStr);
            passed = Boolean.parseBoolean(passedStr);
        } catch (Exception e) {
            req.setAttribute("error", "Invalid number format (enrolmentId / assessmentId / gradePoint).");
            req.getRequestDispatcher("/admin/results/create.jsp").forward(req, resp);
            return;
        }

        StudentResult r = new StudentResult();
        r.setEnrolmentId(enrolmentId);
        r.setAssessmentId(assessmentId);
        r.setGrade(grade.trim());
        r.setGradePoint(gradePoint);
        r.setPassed(passed);

        boolean ok = resultDAO.create(r);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/admin/results");
        } else {
            req.setAttribute("error", "Failed to create result (check FK enrolment_id / assessment_id exists).");
            req.getRequestDispatcher("/admin/results/create.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
