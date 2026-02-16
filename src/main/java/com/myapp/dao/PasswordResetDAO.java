package com.myapp.dao;

import java.sql.*;
import java.time.Instant;

public class PasswordResetDAO {

    private static final String URL =
        "jdbc:mysql://localhost:3306/crs_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "crs_user";
    private static final String PASS = "crs12345";

    public void createToken(int userId, String token, Instant expiresAt) {
        String sql = "INSERT INTO password_resets(user_id, token, expires_at) VALUES (?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setString(2, token);
                ps.setTimestamp(3, Timestamp.from(expiresAt));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Integer findUserIdByValidToken(String token) {
        String sql = "SELECT user_id FROM password_resets WHERE token=? AND expires_at > NOW()";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, token);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("user_id");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteToken(String token) {
        String sql = "DELETE FROM password_resets WHERE token=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, token);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
