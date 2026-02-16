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
import java.util.List;

@WebServlet("/admin/assessments")
public class AssessmentListServlet extends HttpServlet {
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

        List<Assessment> list = assessmentDAO.findByCourseCode(courseCode);

        req.setAttribute("course", course);
        req.setAttribute("assessments", list);
        req.getRequestDispatcher("/admin/assessments/list.jsp").forward(req, resp);
    }
}
