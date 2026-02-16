package com.myapp.servlet.admin;

import com.myapp.dao.MilestoneDAO;
import com.myapp.model.Milestone;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/milestones/delete")
public class MilestoneDeleteServlet extends HttpServlet {
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

        milestoneDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/milestones?plan_id=" + m.getPlanId());
    }
}
