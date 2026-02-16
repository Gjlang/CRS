package com.myapp.dao;

import com.myapp.model.Enrolment;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrolmentDAO {

    // =========================
    // 1) Find all PENDING enrolments
    // =========================
    public List<Enrolment> findPending() {
        List<Enrolment> list = new ArrayList<>();

        String sql = """
            SELECT enrolment_id, student_id, course_code, semester, academic_year, attempt_number, status
            FROM enrollments
            WHERE status = 'PENDING'
            ORDER BY enrolment_id DESC
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRowBasic(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // 2) Find enrolment by ID
    // =========================
    public Enrolment findById(int id) {
        String sql = """
            SELECT enrolment_id, student_id, course_code, semester, academic_year, attempt_number, status
            FROM enrollments
            WHERE enrolment_id = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowBasic(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 3) Approve / Reject
    // =========================
    public boolean approve(int id, int academicUserId) {
        return setStatus(id, "APPROVED", academicUserId);
    }

    public boolean reject(int id, int academicUserId) {
        return setStatus(id, "REJECTED", academicUserId);
    }

    // =========================
    // 4) Set status (robust fallback)
    // =========================
    public boolean setStatus(int id, String status, int academicUserId) {

        String sqlFull = """
            UPDATE enrollments
            SET status = ?, approved_by = ?, approved_at = NOW()
            WHERE enrolment_id = ? AND status = 'PENDING'
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlFull)) {

            ps.setString(1, status);
            ps.setInt(2, academicUserId);
            ps.setInt(3, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException fullFail) {

            String sqlSimple = """
                UPDATE enrollments
                SET status = ?
                WHERE enrolment_id = ? AND status = 'PENDING'
            """;

            try (Connection conn = DbUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlSimple)) {

                ps.setString(1, status);
                ps.setInt(2, id);

                return ps.executeUpdate() == 1;

            } catch (SQLException e2) {
                e2.printStackTrace();
                return false;
            }
        }
    }

    // =========================
    // 5) Get max attempt number for a student + course
    // =========================
    public int getMaxAttempt(String studentId, String courseCode) {

        String sql = """
            SELECT COALESCE(MAX(attempt_number), 0) AS max_attempt
            FROM enrollments
            WHERE student_id = ?
              AND course_code = ?
        """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, courseCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_attempt");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =========================
    // 6) Create a new PENDING enrolment
    // =========================
    public int createPending(String studentId, String courseCode, int attemptNo) {

        String sql = """
            INSERT INTO enrollments
            (student_id, course_code, semester, academic_year, attempt_number, status)
            VALUES (?, ?, ?, ?, ?, 'PENDING')
        """;

        String semester = "1";
        String academicYear = "2026";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            ps.setString(3, semester);
            ps.setString(4, academicYear);
            ps.setInt(5, attemptNo);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // =========================
    // Map
    // =========================
    private Enrolment mapRowBasic(ResultSet rs) throws SQLException {
        Enrolment e = new Enrolment();
        e.setEnrolmentId(rs.getInt("enrolment_id"));
        e.setStudentId(rs.getString("student_id"));
        e.setCourseCode(rs.getString("course_code"));
        e.setSemester(rs.getString("semester"));
        e.setAcademicYear(rs.getString("academic_year"));
        e.setAttemptNumber(rs.getInt("attempt_number"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}