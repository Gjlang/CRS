package com.myapp.servlet.admin;

import com.myapp.dao.StudentResultDAO;
import com.myapp.model.StudentResult;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/results/edit")
public class ResultEditServlet extends HttpServlet {
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

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/results");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/results");
            return;
        }

        StudentResult r = resultDAO.findById(id);
        if (r == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/results");
            return;
        }

        req.setAttribute("result", r);
        req.getRequestDispatcher("/admin/results/edit.jsp").forward(req, resp);
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

        String idStr = req.getParameter("resultId");
        String grade = req.getParameter("grade");
        String gpStr = req.getParameter("gradePoint");
        String passedStr = req.getParameter("passed");

        if (isBlank(idStr) || isBlank(grade) || isBlank(gpStr) || isBlank(passedStr)) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/admin/results/edit.jsp").forward(req, resp);
            return;
        }

        int id;
        double gradePoint;
        boolean passed;

        try {
            id = Integer.parseInt(idStr.trim());
            gradePoint = Double.parseDouble(gpStr);
            passed = Boolean.parseBoolean(passedStr);
        } catch (Exception e) {
            req.setAttribute("error", "Invalid number format.");
            req.getRequestDispatcher("/admin/results/edit.jsp").forward(req, resp);
            return;
        }

        StudentResult r = resultDAO.findById(id);
        if (r == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/results");
            return;
        }

        // Only update editable fields
        r.setGrade(grade.trim());
        r.setGradePoint(gradePoint);
        r.setPassed(passed);

        boolean ok = resultDAO.update(r);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/admin/results");
        } else {
            req.setAttribute("error", "Failed to update result.");
            req.setAttribute("result", r);
            req.getRequestDispatcher("/admin/results/edit.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
