package com.myapp.dao;

import com.myapp.model.Assessment;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssessmentDAO {

    public List<Assessment> findByCourseCode(String courseCode) {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT assessment_id, course_code, component_name, weight_percentage " +
                     "FROM assessments WHERE course_code = ? ORDER BY assessment_id";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseCode);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Assessment findById(int assessmentId) {
        String sql = "SELECT assessment_id, course_code, component_name, weight_percentage " +
                     "FROM assessments WHERE assessment_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assessmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean create(Assessment a) {
        String sql = "INSERT INTO assessments (course_code, component_name, weight_percentage) VALUES (?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getCourseCode());
            ps.setString(2, a.getComponentName());
            ps.setDouble(3, a.getWeightPercentage());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Assessment a) {
        String sql = "UPDATE assessments SET component_name = ?, weight_percentage = ? WHERE assessment_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getComponentName());
            ps.setDouble(2, a.getWeightPercentage());
            ps.setInt(3, a.getAssessmentId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int assessmentId) {
        String sql = "DELETE FROM assessments WHERE assessment_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assessmentId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Assessment mapRow(ResultSet rs) throws SQLException {
        Assessment a = new Assessment();
        a.setAssessmentId(rs.getInt("assessment_id"));
        a.setCourseCode(rs.getString("course_code"));
        a.setComponentName(rs.getString("component_name"));
        a.setWeightPercentage(rs.getDouble("weight_percentage"));
        return a;
    }
}
