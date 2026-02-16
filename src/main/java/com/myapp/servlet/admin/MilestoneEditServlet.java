package com.myapp.servlet.admin;

import com.myapp.dao.MilestoneDAO;
import com.myapp.model.Milestone;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/admin/milestones/edit")
public class MilestoneEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final MilestoneDAO milestoneDAO = new MilestoneDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ADMIN".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        int id;
        try { id = Integer.parseInt(req.getParameter("id").trim()); }
        catch (Exception e) { resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans"); return; }

        Milestone m = milestoneDAO.findById(id);
        if (m == null) { resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans"); return; }

        req.setAttribute("milestone", m);
        req.getRequestDispatcher("/admin/milestones/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ADMIN".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        int id, planId;
        try {
            id = Integer.parseInt(req.getParameter("milestoneId").trim());
            planId = Integer.parseInt(req.getParameter("planId").trim());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/recovery-plans");
            return;
        }

        String weekRange = req.getParameter("weekRange");
        String taskDescription = req.getParameter("taskDescription");
        String due = req.getParameter("dueDate");
        String progressStatus = req.getParameter("progressStatus");

        Date dueDate = null;
        if (due != null && !due.trim().isEmpty()) {
            try { dueDate = Date.valueOf(due.trim()); } catch (Exception ignore) {}
        }

        Milestone m = new Milestone();
        m.setMilestoneId(id);
        m.setPlanId(planId);
        m.setWeekRange(weekRange != null ? weekRange.trim() : "");
        m.setTaskDescription(taskDescription != null ? taskDescription.trim() : "");
        m.setDueDate(dueDate);
        m.setProgressStatus(progressStatus);

        milestoneDAO.update(m);

        resp.sendRedirect(req.getContextPath() + "/admin/milestones?plan_id=" + planId);
    }
}
