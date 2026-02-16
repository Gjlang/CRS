package com.myapp.dao;

import com.myapp.model.User;

import java.sql.*;

public class UserDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/crs_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "crs_user";
    private static final String PASS = "crs12345";

    // =========================
    // 0) Login (your original)
    // =========================
    public User login(String email, String password) {
        String sql = "SELECT user_id, name, email, role, status " +
                     "FROM users WHERE email=? AND password_hash=? AND status='ACTIVE'";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return mapUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // =========================
    // 1) Find by Email (needed for reset)
    // =========================
    public User findByEmail(String email) {
        String sql = "SELECT user_id, name, email, role, status " +
                     "FROM users WHERE email=? LIMIT 1";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return mapUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // =========================
    // 2) Update password_hash (needed for reset)
    // =========================
    public boolean updatePasswordHash(int userId, String newPassword) {
        String sql = "UPDATE users SET password_hash=? WHERE user_id=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, newPassword);
                ps.setInt(2, userId);

                return ps.executeUpdate() == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // 3) Create user (needed for admin user management)
    // =========================
    public int create(User user, String password) {
        String sql = "INSERT INTO users(name, email, role, status, password_hash) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getRole());
                ps.setString(4, user.getStatus() == null ? "ACTIVE" : user.getStatus());
                ps.setString(5, password);

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // =========================
    // 4) Update user (optional)
    // =========================
    public boolean update(User user) {
        String sql = "UPDATE users SET name=?, email=?, role=?, status=? WHERE user_id=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getRole());
                ps.setString(4, user.getStatus());
                ps.setInt(5, user.getUserId());

                return ps.executeUpdate() == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // 5) Deactivate user (optional)
    // =========================
    public boolean deactivate(int userId) {
        String sql = "UPDATE users SET status='INACTIVE' WHERE user_id=?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);
                return ps.executeUpdate() == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // Helper mapper
    // =========================
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        u.setStatus(rs.getString("status"));
        return u;
    }
}
