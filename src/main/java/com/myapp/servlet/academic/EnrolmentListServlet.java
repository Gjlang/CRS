package com.myapp.servlet.academic;

import com.myapp.dao.EnrolmentDAO;
import com.myapp.model.Enrolment;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/academic/enrolments")
public class EnrolmentListServlet extends HttpServlet {
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

        List<Enrolment> pending = enrolmentDAO.findPending();
        req.setAttribute("pendingEnrolments", pending);

        req.getRequestDispatcher("/academic/enrolments/list.jsp").forward(req, resp);
    }
}
