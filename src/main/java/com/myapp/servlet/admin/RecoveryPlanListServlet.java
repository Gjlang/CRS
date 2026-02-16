package com.myapp.servlet.admin;

import com.myapp.dao.RecoveryPlanDAO;
import com.myapp.model.RecoveryPlan;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/recovery-plans")
public class RecoveryPlanListServlet extends HttpServlet {
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

        List<RecoveryPlan> plans = planDAO.findAll();
        req.setAttribute("plans", plans);

        req.getRequestDispatcher("/admin/recovery-plans/list.jsp").forward(req, resp);
    }
}
