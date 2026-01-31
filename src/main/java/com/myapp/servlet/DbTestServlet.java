package com.myapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dbtest")
public class DbTestServlet extends HttpServlet {

    private static final String URL =
        "jdbc:mysql://localhost:3306/crs_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "crs_user";
    private static final String PASS = "crs12345";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        // Step 1: Check if driver is loaded
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            resp.getWriter().println("❌ DRIVER NOT FOUND (jar not loaded)");
            resp.getWriter().println(e.toString());
            return;
        }

        // Step 2: Try to connect
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            resp.getWriter().println("✅ CONNECTED TO MySQL! YEYYY");
        } catch (Exception e) {
            resp.getWriter().println("❌ FAILED:");
            resp.getWriter().println(e.toString());
        }
    }
}