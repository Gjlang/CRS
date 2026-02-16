package com.myapp.dao;

import com.myapp.model.Milestone;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MilestoneDAO {

    public List<Milestone> findByPlanId(int planId) {
        List<Milestone> list = new ArrayList<>();

        String sql = """
            SELECT milestone_id, plan_id, week_range, task_description, due_date, progress_status, updated_at
            FROM milestones
            WHERE plan_id = ?
            ORDER BY due_date IS NULL, due_date ASC, milestone_id ASC
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

    public Milestone findById(int id) {
        String sql = """
            SELECT milestone_id, plan_id, week_range, task_description, due_date, progress_status, updated_at
            FROM milestones
            WHERE milestone_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int create(Milestone m) {
        String sql = """
            INSERT INTO milestones (plan_id, week_range, task_description, due_date, progress_status)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getPlanId());
            ps.setString(2, m.getWeekRange());
            ps.setString(3, m.getTaskDescription());
            ps.setDate(4, m.getDueDate()); // can be null
            ps.setString(5, normalizeStatus(m.getProgressStatus()));

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

    public boolean update(Milestone m) {
        String sql = """
            UPDATE milestones
            SET week_range = ?, task_description = ?, due_date = ?, progress_status = ?
            WHERE milestone_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getWeekRange());
            ps.setString(2, m.getTaskDescription());
            ps.setDate(3, m.getDueDate());
            ps.setString(4, normalizeStatus(m.getProgressStatus()));
            ps.setInt(5, m.getMilestoneId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM milestones WHERE milestone_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Academic recommended: only change progress_status
    public boolean setProgressStatus(int id, String progressStatus) {
        String sql = "UPDATE milestones SET progress_status = ? WHERE milestone_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, normalizeStatus(progressStatus));
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Milestone mapRow(ResultSet rs) throws SQLException {
        Milestone m = new Milestone();
        m.setMilestoneId(rs.getInt("milestone_id"));
        m.setPlanId(rs.getInt("plan_id"));
        m.setWeekRange(rs.getString("week_range"));
        m.setTaskDescription(rs.getString("task_description"));
        m.setDueDate(rs.getDate("due_date"));
        m.setProgressStatus(rs.getString("progress_status"));
        m.setUpdatedAt(rs.getTimestamp("updated_at"));
        return m;
    }

    private String normalizeStatus(String s) {
        if (s == null || s.trim().isEmpty()) return "PENDING";
        return s.trim().toUpperCase();
    }
}
