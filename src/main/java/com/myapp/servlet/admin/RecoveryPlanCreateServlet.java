package com.myapp.servlet.admin;

import com.myapp.dao.RecoveryPlanDAO;
import com.myapp.model.RecoveryPlan;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/recovery-plans/create")
public class RecoveryPlanCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final RecoveryPlanDAO planDAO = new RecoveryPlanDAO();

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

        req.getRequestDispatcher("/admin/recovery-plans/create.jsp").forward(req, resp);
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

        String enrolStr = req.getParameter("enrolmentId");
        String createdByStr = req.getParameter("createdBy");
        String status = req.getParameter("status");
        String remarks = req.getParameter("remarks");

        if (enrolStr == null || enrolStr.trim().isEmpty() || createdByStr == null || createdByStr.trim().isEmpty()) {
            req.setAttribute("error", "Enrolment ID and Created By (user_id) are required.");
            req.getRequestDispatcher("/admin/recovery-plans/create.jsp").forward(req, resp);
            return;
        }

        int enrolmentId, createdBy;
        try {
            enrolmentId = Integer.parseInt(enrolStr.trim());
            createdBy = Integer.parseInt(createdByStr.trim());
        } catch (Exception e) {
            req.setAttribute("error", "Invalid number format for enrolmentId / createdBy.");
            req.getRequestDispatcher("/admin/recovery-plans/create.jsp").forward(req, resp);
            return;
        }

        RecoveryPlan p = new RecoveryPlan();
        p.setEnrolmentId(enrolmentId);
        p.setCreatedBy(createdBy);
        p.setStatus(status);
        p.setRemarks(remarks);

        int newId = planDAO.create(p);
        if (newId > 0) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans/view?id=" + newId);
        } else {
            req.setAttribute("error", "Create failed. Check enrolment_id exists and created_by exists in users.");
            req.getRequestDispatcher("/admin/recovery-plans/create.jsp").forward(req, resp);
        }
    }
}
