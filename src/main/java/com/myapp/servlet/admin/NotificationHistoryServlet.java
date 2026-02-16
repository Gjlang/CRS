package com.myapp.servlet.admin;

import com.myapp.dao.NotificationDAO;
import com.myapp.model.Notification;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/notifications/history")
public class NotificationHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        if (!"ADMIN".equals(user.getRole())) { resp.sendRedirect(req.getContextPath() + "/dashboard"); return; }

        List<Notification> list = notificationDAO.findAll();
        req.setAttribute("notifications", list);

        req.getRequestDispatcher("/admin/notifications/history.jsp").forward(req, resp);
    }
}
