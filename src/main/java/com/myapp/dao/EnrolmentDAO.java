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
    // academicUserId can be stored if DB has approved_by column
    // =========================
    public boolean approve(int id, int academicUserId) {
        return setStatus(id, "APPROVED", academicUserId);
    }

    public boolean reject(int id, int academicUserId) {
        return setStatus(id, "REJECTED", academicUserId);
    }

    // =========================
    // 4) Set status (robust fallback)
    // Try update status + approved_by + approved_at
    // If columns not exist -> update status only
    // =========================
    public boolean setStatus(int id, String status, int academicUserId) {

        // Try full update first
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

            // Fallback: status only
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
