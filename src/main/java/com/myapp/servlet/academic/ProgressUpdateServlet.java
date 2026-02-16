package com.myapp.servlet.academic;

import com.myapp.dao.MilestoneDAO;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/academic/progress/update")
public class ProgressUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final MilestoneDAO milestoneDAO = new MilestoneDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ACADEMIC".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        int planId, milestoneId;
        try {
            planId = Integer.parseInt(req.getParameter("planId").trim());
            milestoneId = Integer.parseInt(req.getParameter("milestoneId").trim());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/academic/progress");
            return;
        }

        String progressStatus = req.getParameter("progressStatus");
        milestoneDAO.setProgressStatus(milestoneId, progressStatus);

        resp.sendRedirect(req.getContextPath() + "/academic/progress?plan_id=" + planId);
    }
}
