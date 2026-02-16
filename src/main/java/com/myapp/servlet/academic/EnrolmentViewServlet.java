package com.myapp.servlet.academic;

import com.myapp.dao.EnrolmentDAO;
import com.myapp.model.Enrolment;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/academic/enrolments/view")
public class EnrolmentViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EnrolmentDAO enrolmentDAO = new EnrolmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"ACADEMIC".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
            return;
        }

        Enrolment enrol = enrolmentDAO.findById(id);
        if (enrol == null) {
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
            return;
        }

        req.setAttribute("enrolment", enrol);
        req.getRequestDispatcher("/academic/enrolments/view.jsp").forward(req, resp);
    }
}
