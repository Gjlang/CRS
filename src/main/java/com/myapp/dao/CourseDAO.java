package com.myapp.dao;

import com.myapp.model.Course;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> findAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_code, title, credit_hours FROM courses ORDER BY course_code";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Course findById(String courseCode) {
        String sql = "SELECT course_code, title, credit_hours FROM courses WHERE course_code = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean create(Course c) {
        String sql = "INSERT INTO courses (course_code, title, credit_hours) VALUES (?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getTitle());
            ps.setInt(3, c.getCreditHours());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Course c) {
        // course_code is PK, do not change it
        String sql = "UPDATE courses SET title = ?, credit_hours = ? WHERE course_code = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getTitle());
            ps.setInt(2, c.getCreditHours());
            ps.setString(3, c.getCourseCode());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Optional deactivate only if you have a status column (you DON'T in screenshot)
    // So we skip deactivate() for now.

    private Course mapRow(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseCode(rs.getString("course_code"));
        c.setTitle(rs.getString("title"));
        c.setCreditHours(rs.getInt("credit_hours"));
        return c;
    }
}
