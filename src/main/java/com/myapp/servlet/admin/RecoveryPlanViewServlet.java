package com.myapp.servlet.admin;

import com.myapp.dao.RecoveryPlanDAO;
import com.myapp.model.RecoveryPlan;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/recovery-plans/view")
public class RecoveryPlanViewServlet extends HttpServlet {
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
        req.getRequestDispatcher("/admin/recovery-plans/view.jsp").forward(req, resp);
    }
}
