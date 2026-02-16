package com.myapp.servlet.admin;

import com.myapp.dao.NotificationDAO;
import com.myapp.dao.RecoveryPlanDAO;
import com.myapp.dao.StudentDAO;
import com.myapp.model.Notification;
import com.myapp.model.RecoveryPlan;
import com.myapp.model.Student;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/notifications/send")
public class NotificationSendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final StudentDAO studentDAO = new StudentDAO();
    private final RecoveryPlanDAO planDAO = new RecoveryPlanDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ADMIN".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        List<Student> students = studentDAO.findAll();
        List<RecoveryPlan> plans = planDAO.findAll();

        req.setAttribute("students", students);
        req.setAttribute("plans", plans);

        req.getRequestDispatcher("/admin/notifications/send.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ADMIN".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        String studentId = req.getParameter("studentId");
        String planIdStr = req.getParameter("planId");
        String title = req.getParameter("title");
        String message = req.getParameter("message");
        String priority = req.getParameter("priority");
        String status = req.getParameter("status");

        Integer planId = null;
        if (planIdStr != null && !planIdStr.trim().isEmpty()) {
            try { planId = Integer.parseInt(planIdStr.trim()); } catch (Exception ignore) {}
        }

        // Validation
        if ((studentId == null || studentId.trim().isEmpty()) && planId == null) {
            req.setAttribute("error", "Please choose Student OR Recovery Plan.");
            doGet(req, resp);
            return;
        }
        if (title == null || title.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            req.setAttribute("error", "Title and Message are required.");
            doGet(req, resp);
            return;
        }

        Notification n = new Notification();
        n.setStudentId((studentId != null && !studentId.trim().isEmpty()) ? studentId.trim() : null);
        n.setPlanId(planId);
        n.setTitle(title.trim());
        n.setMessage(message.trim());
        n.setPriority(priority);
        n.setStatus(status);
        n.setCreatedBy(user.getUserId()); // IMPORTANT: your User has getUserId()

        notificationDAO.create(n);

        resp.sendRedirect(req.getContextPath() + "/admin/notifications/history");
    }
}
