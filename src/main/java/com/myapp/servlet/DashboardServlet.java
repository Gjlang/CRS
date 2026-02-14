package com.myapp.servlet;

import com.myapp.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession session = req.getSession(false);
    User user = (session == null) ? null : (User) session.getAttribute("user");

    if (user == null) {
      resp.sendRedirect(req.getContextPath() + "/login.jsp");
      return;
    }

    if ("ADMIN".equals(user.getRole())) {
      resp.sendRedirect(req.getContextPath() + "/admin/dashboard.jsp");
    } else if ("ACADEMIC".equals(user.getRole())) {
      resp.sendRedirect(req.getContextPath() + "/academic/dashboard.jsp");
    } else {
      resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
  }
}
