package com.myapp.servlet.admin;

import com.myapp.dao.RecoveryPlanDAO;
import com.myapp.model.RecoveryPlan;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/recovery-plans/edit")
public class RecoveryPlanEditServlet extends HttpServlet {
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

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans");
            return;
        }

        RecoveryPlan plan = planDAO.findById(id);
        if (plan == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans");
            return;
        }

        req.setAttribute("plan", plan);
        req.getRequestDispatcher("/admin/recovery-plans/edit.jsp").forward(req, resp);
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

        String planIdStr = req.getParameter("planId");
        String enrolStr = req.getParameter("enrolmentId");
        String status = req.getParameter("status");
        String remarks = req.getParameter("remarks");

        int planId, enrolmentId;
        try {
            planId = Integer.parseInt(planIdStr.trim());
            enrolmentId = Integer.parseInt(enrolStr.trim());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans");
            return;
        }

        RecoveryPlan p = new RecoveryPlan();
        p.setPlanId(planId);
        p.setEnrolmentId(enrolmentId);
        p.setStatus(status);
        p.setRemarks(remarks);

        boolean ok = planDAO.update(p);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans/view?id=" + planId);
        } else {
            req.setAttribute("error", "Update failed. Check enrolment_id exists.");
            req.setAttribute("plan", p);
            req.getRequestDispatcher("/admin/recovery-plans/edit.jsp").forward(req, resp);
        }
    }
}
