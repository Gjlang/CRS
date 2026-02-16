package com.myapp.servlet.academic;

import com.myapp.dao.NotificationDAO;
import com.myapp.model.Notification;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/academic/notifications")
public class AcademicNotificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ACADEMIC".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        String studentId = req.getParameter("student_id");
        String planStr = req.getParameter("plan_id");

        List<Notification> list = null;

        if (studentId != null && !studentId.trim().isEmpty()) {
            list = notificationDAO.findByStudentId(studentId.trim());
            req.setAttribute("studentId", studentId.trim());
        } else if (planStr != null && !planStr.trim().isEmpty()) {
            try {
                int planId = Integer.parseInt(planStr.trim());
                list = notificationDAO.findByPlanId(planId);
                req.setAttribute("planId", planId);
            } catch (Exception ignore) {
                // keep list null
            }
        }

        req.setAttribute("notifications", list);
        req.getRequestDispatcher("/academic/notifications.jsp").forward(req, resp);
    }
}
