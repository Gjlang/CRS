package com.myapp.dao;

import com.myapp.model.Notification;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public int create(Notification n) {
        String sql = """
            INSERT INTO notifications (student_id, plan_id, title, message, priority, status, created_by)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // student_id nullable
            if (n.getStudentId() == null || n.getStudentId().trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, n.getStudentId().trim());
            }

            // plan_id nullable
            if (n.getPlanId() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, n.getPlanId());

            ps.setString(3, n.getTitle());
            ps.setString(4, n.getMessage());
            ps.setString(5, normalizePriority(n.getPriority()));
            ps.setString(6, normalizeStatus(n.getStatus()));
            ps.setInt(7, n.getCreatedBy());

            int affected = ps.executeUpdate();
            if (affected != 1) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Admin: history
    public List<Notification> findAll() {
        List<Notification> list = new ArrayList<>();

        String sql =
        		  "SELECT notification_id, student_id, plan_id, title, message, priority, status, created_by, created_at " +
        		  "FROM notifications ORDER BY created_at DESC";


        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Notification> findByStudentId(String studentId) {
        List<Notification> list = new ArrayList<>();

        String sql = """
            SELECT notification_id, student_id, plan_id, title, message, priority, status, created_by, created_at
            FROM notifications
            WHERE student_id = ?
            ORDER BY created_at DESC, notification_id DESC
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Notification> findByPlanId(int planId) {
        List<Notification> list = new ArrayList<>();

        String sql = """
            SELECT notification_id, student_id, plan_id, title, message, priority, status, created_by, created_at
            FROM notifications
            WHERE plan_id = ?
            ORDER BY created_at DESC, notification_id DESC
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, planId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setNotificationId(rs.getInt("notification_id"));
        n.setStudentId(rs.getString("student_id"));

        int pid = rs.getInt("plan_id");
        n.setPlanId(rs.wasNull() ? null : pid);

        n.setTitle(rs.getString("title"));
        n.setMessage(rs.getString("message"));
        n.setPriority(rs.getString("priority"));
        n.setStatus(rs.getString("status"));
        n.setCreatedBy(rs.getInt("created_by"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        return n;
    }

    private String normalizePriority(String p) {
        if (p == null || p.trim().isEmpty()) return "NORMAL";
        return p.trim().toUpperCase();
    }

    private String normalizeStatus(String s) {
        if (s == null || s.trim().isEmpty()) return "SENT";
        return s.trim().toUpperCase();
    }
}
