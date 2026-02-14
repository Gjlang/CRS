package com.myapp.servlet;

import com.myapp.dao.UserDAO;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        System.out.println("LOGIN email=" + email + " pass=" + password);


        User user = userDAO.login(email, password);

        if (user == null) {
            req.setAttribute("error", "Login failed. Wrong email/password or inactive user.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        req.getSession(true).setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
    }
}
