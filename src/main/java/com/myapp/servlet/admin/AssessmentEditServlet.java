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

@WebServlet("/admin/assessments/edit")
public class AssessmentEditServlet extends HttpServlet {
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

        String idStr = req.getParameter("id");
        if (isBlank(idStr)) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        Assessment a = assessmentDAO.findById(id);
        if (a == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        Course course = courseDAO.findById(a.getCourseCode());
        req.setAttribute("course", course);
        req.setAttribute("assessment", a);
        req.getRequestDispatcher("/admin/assessments/edit.jsp").forward(req, resp);
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

        String idStr = req.getParameter("assessmentId");
        String courseCode = req.getParameter("courseCode");
        String componentName = req.getParameter("componentName");
        String weightStr = req.getParameter("weightPercentage");

        if (isBlank(idStr) || isBlank(courseCode) || isBlank(componentName) || isBlank(weightStr)) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/admin/assessments/edit.jsp").forward(req, resp);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid assessment id.");
            req.getRequestDispatcher("/admin/assessments/edit.jsp").forward(req, resp);
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Weight must be a number.");
            req.getRequestDispatcher("/admin/assessments/edit.jsp").forward(req, resp);
            return;
        }

        Assessment a = new Assessment();
        a.setAssessmentId(id);
        a.setCourseCode(courseCode.trim());
        a.setComponentName(componentName.trim());
        a.setWeightPercentage(weight);

        boolean ok = assessmentDAO.update(a);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/admin/assessments?course_code=" + courseCode.trim());
        } else {
            req.setAttribute("error", "Failed to update assessment.");
            req.setAttribute("course", courseDAO.findById(courseCode));
            req.setAttribute("assessment", a);
            req.getRequestDispatcher("/admin/assessments/edit.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
