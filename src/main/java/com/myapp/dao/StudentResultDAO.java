package com.myapp.dao;

import com.myapp.model.StudentResult;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentResultDAO {

    // =========================
    // 1) FIND ALL (Admin View)
    // =========================
    public List<StudentResult> findAll() {
        List<StudentResult> list = new ArrayList<>();

        String sql = """
            SELECT r.result_id, r.enrolment_id, r.assessment_id,
                   r.grade, r.grade_point, r.passed,
                   e.student_id, e.course_code,
                   a.component_name
            FROM student_results r
            JOIN enrollments e ON r.enrolment_id = e.enrolment_id
            JOIN assessments a ON r.assessment_id = a.assessment_id
            ORDER BY r.result_id
        """;

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

    // =========================
    // 2) FIND BY STUDENT ID
    // =========================
    public List<StudentResult> findByStudentId(String studentId) {
        List<StudentResult> list = new ArrayList<>();

        String sql = """
            SELECT r.result_id, r.enrolment_id, r.assessment_id,
                   r.grade, r.grade_point, r.passed,
                   e.student_id, e.course_code,
                   a.component_name
            FROM student_results r
            JOIN enrollments e ON r.enrolment_id = e.enrolment_id
            JOIN assessments a ON r.assessment_id = a.assessment_id
            WHERE e.student_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

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

    // =========================
    // 3) FIND BY ID
    // =========================
    public StudentResult findById(int id) {

        String sql = """
            SELECT result_id, enrolment_id, assessment_id,
                   grade, grade_point, passed
            FROM student_results
            WHERE result_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StudentResult r = new StudentResult();
                    r.setResultId(rs.getInt("result_id"));
                    r.setEnrolmentId(rs.getInt("enrolment_id"));
                    r.setAssessmentId(rs.getInt("assessment_id"));
                    r.setGrade(rs.getString("grade"));
                    r.setGradePoint(rs.getDouble("grade_point"));
                    r.setPassed(rs.getBoolean("passed"));
                    return r;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 4) CREATE
    // =========================
    public boolean create(StudentResult r) {

        String sql = """
            INSERT INTO student_results
            (enrolment_id, assessment_id, grade, grade_point, passed)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getEnrolmentId());
            ps.setInt(2, r.getAssessmentId());
            ps.setString(3, r.getGrade());
            ps.setDouble(4, r.getGradePoint());
            ps.setBoolean(5, r.isPassed());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // 5) UPDATE
    // =========================
    public boolean update(StudentResult r) {

        String sql = """
            UPDATE student_results
            SET grade = ?, grade_point = ?, passed = ?
            WHERE result_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getGrade());
            ps.setDouble(2, r.getGradePoint());
            ps.setBoolean(3, r.isPassed());
            ps.setInt(4, r.getResultId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // 6) DELETE
    // =========================
    public boolean delete(int id) {

        String sql = "DELETE FROM student_results WHERE result_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // MAP
    // =========================
    private StudentResult mapRow(ResultSet rs) throws SQLException {
        StudentResult r = new StudentResult();

        r.setResultId(rs.getInt("result_id"));
        r.setEnrolmentId(rs.getInt("enrolment_id"));
        r.setAssessmentId(rs.getInt("assessment_id"));
        r.setGrade(rs.getString("grade"));
        r.setGradePoint(rs.getDouble("grade_point"));
        r.setPassed(rs.getBoolean("passed"));
        r.setStudentId(rs.getString("student_id"));
        r.setCourseCode(rs.getString("course_code"));
        r.setComponentName(rs.getString("component_name"));

        return r;
    }
}
