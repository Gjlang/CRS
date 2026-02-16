package com.myapp.servlet.academic;

import com.myapp.dao.MilestoneDAO;
import com.myapp.model.Milestone;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/academic/progress")
public class ProgressViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final MilestoneDAO milestoneDAO = new MilestoneDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ACADEMIC".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        String planStr = req.getParameter("plan_id");

        if (planStr == null || planStr.trim().isEmpty()) {
            req.getRequestDispatcher("/academic/progress/view.jsp").forward(req, resp);
            return;
        }

        int planId;
        try { planId = Integer.parseInt(planStr.trim()); }
        catch (Exception e) {
            req.getRequestDispatcher("/academic/progress/view.jsp").forward(req, resp);
            return;
        }

        List<Milestone> milestones = milestoneDAO.findByPlanId(planId);
        req.setAttribute("planId", planId);
        req.setAttribute("milestones", milestones);

        req.getRequestDispatcher("/academic/progress/view.jsp").forward(req, resp);
    }
}
