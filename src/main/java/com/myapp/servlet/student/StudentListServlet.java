package com.myapp.servlet.student;

import com.myapp.dao.StudentDAO;
import com.myapp.model.Student;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/students")
public class StudentListServlet extends HttpServlet {
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

        List<Student> students = studentDAO.findAll();
        req.setAttribute("students", students);

        req.getRequestDispatcher("/admin/students/list.jsp").forward(req, resp);
    }
}
