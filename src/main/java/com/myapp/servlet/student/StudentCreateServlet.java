package com.myapp.servlet.student;

import com.myapp.dao.StudentDAO;
import com.myapp.model.Student;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/students/create")
public class StudentCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Session check
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Role check
        if (!"ADMIN".equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "ADMIN only");
            return;
        }

        req.getRequestDispatcher("/admin/students/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	System.out.println(">>> HIT CREATE doPost()");
    	System.out.println("studentId=" + req.getParameter("studentId"));
    	System.out.println("name=" + req.getParameter("name"));
    	System.out.println("program=" + req.getParameter("program"));
    	System.out.println("yearOfStudy=" + req.getParameter("yearOfStudy"));
    	System.out.println("email=" + req.getParameter("email"));


        // Session check
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Role check
        if (!"ADMIN".equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "ADMIN only");
            return;
        }

        // Read form data
        String studentId = req.getParameter("studentId");
        String name = req.getParameter("name");
        String program = req.getParameter("program");
        String yearStr = req.getParameter("yearOfStudy");
        String email = req.getParameter("email");

        // Basic validation
        if (isBlank(studentId) || isBlank(name) || isBlank(program) || isBlank(yearStr) || isBlank(email)) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/admin/students/create.jsp").forward(req, resp);
            return;
        }

        int yearOfStudy;
        try {
            yearOfStudy = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Year must be a number.");
            req.getRequestDispatcher("/admin/students/create.jsp").forward(req, resp);
            return;
        }

        // Create student (default ACTIVE)
        Student s = new Student();
        s.setStudentId(studentId.trim());
        s.setName(name.trim());
        s.setProgram(program.trim());
        s.setYearOfStudy(yearOfStudy);
        s.setEmail(email.trim());
        s.setStatus("ACTIVE");

        // Insert
        try {
            studentDAO.create(s);
            resp.sendRedirect(req.getContextPath() + "/admin/students");
        } catch (Exception ex) {
            // Common: duplicate student_id
            req.setAttribute("error", "Failed to create student. (Maybe Student ID already exists)");
            req.getRequestDispatcher("/admin/students/create.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
