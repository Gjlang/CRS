package com.myapp.servlet.admin;

import com.myapp.dao.MilestoneDAO;
import com.myapp.model.Milestone;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/milestones")
public class MilestoneListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final MilestoneDAO milestoneDAO = new MilestoneDAO();

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

        String planStr = req.getParameter("plan_id");
        if (planStr == null || planStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans");
            return;
        }

        int planId;
        try {
            planId = Integer.parseInt(planStr.trim());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans");
            return;
        }

        List<Milestone> milestones = milestoneDAO.findByPlanId(planId);
        req.setAttribute("planId", planId);
        req.setAttribute("milestones", milestones);

        req.getRequestDispatcher("/admin/milestones/list.jsp").forward(req, resp);
    }
}
