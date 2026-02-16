package com.myapp.servlet.admin;

import com.myapp.dao.CourseDAO;
import com.myapp.model.Course;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/courses/edit")
public class CourseEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        String code = req.getParameter("course_code");
        if (isBlank(code)) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        Course c = courseDAO.findById(code.trim());
        if (c == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
            return;
        }

        req.setAttribute("course", c);
        req.getRequestDispatcher("/admin/courses/edit.jsp").forward(req, resp);
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
        String title = req.getParameter("title");
        String creditStr = req.getParameter("creditHours");

        if (isBlank(courseCode) || isBlank(title) || isBlank(creditStr)) {
            req.setAttribute("error", "All fields are required.");
            Course back = new Course(courseCode, title, 0);
            req.setAttribute("course", back);
            req.getRequestDispatcher("/admin/courses/edit.jsp").forward(req, resp);
            return;
        }

        int creditHours;
        try {
            creditHours = Integer.parseInt(creditStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Credit Hours must be a number.");
            Course back = new Course(courseCode, title, 0);
            req.setAttribute("course", back);
            req.getRequestDispatcher("/admin/courses/edit.jsp").forward(req, resp);
            return;
        }

        Course c = new Course(courseCode.trim(), title.trim(), creditHours);

        boolean ok = courseDAO.update(c);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/admin/courses");
        } else {
            req.setAttribute("error", "Failed to update course.");
            req.setAttribute("course", c);
            req.getRequestDispatcher("/admin/courses/edit.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
