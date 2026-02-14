package com.myapp.dao;

import com.myapp.model.User;

import java.sql.*;

public class UserDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/crs_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "crs_user";
    private static final String PASS = "crs12345";

    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT user_id, name, email, role, status " +
                     "FROM users " +
                     "WHERE email=? AND password_hash=? AND status='ACTIVE'";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setStatus(rs.getString("status"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
