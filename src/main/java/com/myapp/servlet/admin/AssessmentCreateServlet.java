package com.myapp.servlet.admin;

import com.myapp.dao.AssessmentDAO;
import com.myapp.dao.CourseDAO;
import com.myapp.model.Assessment;
import com.myapp.model.Course;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/assessments/create")
public class AssessmentCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final AssessmentDAO assessmentDAO = new AssessmentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

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

        String courseCode = req.getParameter("course_code");
        if (courseCode == null || courseCode.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        courseCode = courseCode.trim();
        Course course = courseDAO.findById(courseCode);
        if (course == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        req.setAttribute("course", course);
        req.getRequestDispatcher("/admin/assessments/create.jsp").forward(req, resp);
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

        String courseCode = req.getParameter("courseCode");
        String componentName = req.getParameter("componentName");
        String weightStr = req.getParameter("weightPercentage");

        if (isBlank(courseCode) || isBlank(componentName) || isBlank(weightStr)) {
            req.setAttribute("error", "All fields are required.");
            req.setAttribute("course", courseDAO.findById(courseCode));
            req.getRequestDispatcher("/admin/assessments/create.jsp").forward(req, resp);
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Weight must be a number.");
            req.setAttribute("course", courseDAO.findById(courseCode));
            req.getRequestDispatcher("/admin/assessments/create.jsp").forward(req, resp);
            return;
        }

        Assessment a = new Assessment();
        a.setCourseCode(courseCode.trim());
        a.setComponentName(componentName.trim());
        a.setWeightPercentage(weight);

        boolean ok = assessmentDAO.create(a);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/admin/assessments?course_code=" + courseCode.trim());
        } else {
            req.setAttribute("error", "Failed to add assessment.");
            req.setAttribute("course", courseDAO.findById(courseCode));
            req.getRequestDispatcher("/admin/assessments/create.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
