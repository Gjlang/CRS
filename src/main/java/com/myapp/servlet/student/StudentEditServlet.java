package com.myapp.servlet.student;

import com.myapp.dao.StudentDAO;
import com.myapp.model.Student;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/students/edit")
public class StudentEditServlet extends HttpServlet {
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

        Student s = studentDAO.findById(id.trim());
        if (s == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/students");
            return;
        }

        req.setAttribute("student", s);
        req.getRequestDispatcher("/admin/students/edit.jsp").forward(req, resp);
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
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "ADMIN only");
            return;
        }

        // student_id cannot change (PK). We'll keep it readonly in JSP.
        String studentId = req.getParameter("studentId");
        String name = req.getParameter("name");
        String program = req.getParameter("program");
        String yearStr = req.getParameter("yearOfStudy");
        String email = req.getParameter("email");
        String status = req.getParameter("status");

        if (isBlank(studentId) || isBlank(name) || isBlank(program) || isBlank(yearStr) || isBlank(email)) {
            req.setAttribute("error", "All fields are required (except status).");
            Student back = new Student();
            back.setStudentId(studentId);
            back.setName(name);
            back.setProgram(program);
            back.setEmail(email);
            back.setStatus(status);
            try { back.setYearOfStudy(Integer.parseInt(yearStr)); } catch (Exception ignored) {}
            req.setAttribute("student", back);
            req.getRequestDispatcher("/admin/students/edit.jsp").forward(req, resp);
            return;
        }

        int yearOfStudy;
        try {
            yearOfStudy = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Year must be a number.");
            Student back = new Student();
            back.setStudentId(studentId);
            back.setName(name);
            back.setProgram(program);
            back.setEmail(email);
            back.setStatus(status);
            req.setAttribute("student", back);
            req.getRequestDispatcher("/admin/students/edit.jsp").forward(req, resp);
            return;
        }

        Student s = new Student();
        s.setStudentId(studentId.trim());
        s.setName(name.trim());
        s.setProgram(program.trim());
        s.setYearOfStudy(yearOfStudy);
        s.setEmail(email.trim());
        s.setStatus(status); // StudentDAO will normalize to ACTIVE/INACTIVE

        boolean ok = studentDAO.update(s);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/admin/students");
        } else {
            req.setAttribute("error", "Failed to update student.");
            req.setAttribute("student", s);
            req.getRequestDispatcher("/admin/students/edit.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
