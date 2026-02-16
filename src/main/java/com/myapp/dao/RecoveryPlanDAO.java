package com.myapp.dao;

import com.myapp.model.RecoveryPlan;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecoveryPlanDAO {

    // =========================
    // 1) FIND ALL (with enrolment info)
    // =========================
    public List<RecoveryPlan> findAll() {
        List<RecoveryPlan> list = new ArrayList<>();

        String sql = """
            SELECT p.plan_id, p.enrolment_id, p.created_by, p.status, p.remarks, p.created_at,
                   e.student_id, e.course_code
            FROM recovery_plans p
            JOIN enrollments e ON p.enrolment_id = e.enrolment_id
            ORDER BY p.plan_id DESC
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // 2) FIND BY ID
    // =========================
    public RecoveryPlan findById(int planId) {
        String sql = """
            SELECT p.plan_id, p.enrolment_id, p.created_by, p.status, p.remarks, p.created_at,
                   e.student_id, e.course_code
            FROM recovery_plans p
            JOIN enrollments e ON p.enrolment_id = e.enrolment_id
            WHERE p.plan_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, planId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 3) CREATE (return new plan_id)
    // =========================
    public int create(RecoveryPlan plan) {
        String sql = """
            INSERT INTO recovery_plans (enrolment_id, created_by, status, remarks)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, plan.getEnrolmentId());
            ps.setInt(2, plan.getCreatedBy());
            ps.setString(3, normalizeStatus(plan.getStatus()));
            ps.setString(4, plan.getRemarks());

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

    // =========================
    // 4) UPDATE
    // =========================
    public boolean update(RecoveryPlan plan) {
        String sql = """
            UPDATE recovery_plans
            SET enrolment_id = ?, status = ?, remarks = ?
            WHERE plan_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, plan.getEnrolmentId());
            ps.setString(2, normalizeStatus(plan.getStatus()));
            ps.setString(3, plan.getRemarks());
            ps.setInt(4, plan.getPlanId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Optional: setStatus
    public boolean setStatus(int planId, String status) {
        String sql = "UPDATE recovery_plans SET status = ? WHERE plan_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, normalizeStatus(status));
            ps.setInt(2, planId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private RecoveryPlan mapRow(ResultSet rs) throws SQLException {
        RecoveryPlan p = new RecoveryPlan();
        p.setPlanId(rs.getInt("plan_id"));
        p.setEnrolmentId(rs.getInt("enrolment_id"));
        p.setCreatedBy(rs.getInt("created_by"));
        p.setStatus(rs.getString("status"));
        p.setRemarks(rs.getString("remarks"));
        p.setCreatedAt(rs.getTimestamp("created_at"));

        p.setStudentId(rs.getString("student_id"));
        p.setCourseCode(rs.getString("course_code"));
        return p;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) return "DRAFT";
        return status.trim().toUpperCase();
    }
}
