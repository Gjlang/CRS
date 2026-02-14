package com.myapp.dao;

import com.myapp.model.Student;
import com.myapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

	// =========================
	// 1) SELECT ALL
	// =========================
	public List<Student> findAll() {
		List<Student> list = new ArrayList<>();

		String sql = "SELECT student_id, name, program, year_of_study, email, status " + "FROM students "
				+ "ORDER BY student_id";

		try (Connection conn = DbUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Student s = mapRow(rs);
				list.add(s);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// =========================
	// 2) SELECT BY ID
	// =========================
	public Student findById(String studentId) {
		String sql = "SELECT student_id, name, program, year_of_study, email, status " + "FROM students "
				+ "WHERE student_id = ?";

		try (Connection conn = DbUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, studentId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// =========================
	// 3) INSERT
	// =========================
	public boolean create(Student s) {
		String sql = "INSERT INTO students (student_id, name, program, year_of_study, email, status) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = DbUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, s.getStudentId());
			ps.setString(2, s.getName());
			ps.setString(3, s.getProgram());
			ps.setInt(4, s.getYearOfStudy());
			ps.setString(5, s.getEmail());
			ps.setString(6, normalizeStatus(s.getStatus())); // default ACTIVE kalau kosong

			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			// duplicate PK / constraint error akan masuk sini
			e.printStackTrace();
			return false;
		}
	}

	// =========================
	// 4) UPDATE (by student_id)
	// =========================
	public boolean update(Student s) {
		String sql = "UPDATE students " + "SET name = ?, program = ?, year_of_study = ?, email = ?, status = ? "
				+ "WHERE student_id = ?";

		try (Connection conn = DbUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, s.getName());
			ps.setString(2, s.getProgram());
			ps.setInt(3, s.getYearOfStudy());
			ps.setString(4, s.getEmail());
			ps.setString(5, normalizeStatus(s.getStatus()));
			ps.setString(6, s.getStudentId());

			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================
	// 5) DEACTIVATE (set status INACTIVE)
	// =========================
	public boolean deactivate(String studentId) {
		String sql = "UPDATE students SET status = 'INACTIVE' WHERE student_id = ?";

		try (Connection conn = DbUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, studentId);
			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================
	// Helper: map ResultSet -> Student
	// =========================
	private Student mapRow(ResultSet rs) throws SQLException {
		Student s = new Student();
		s.setStudentId(rs.getString("student_id"));
		s.setName(rs.getString("name"));
		s.setProgram(rs.getString("program"));
		s.setYearOfStudy(rs.getInt("year_of_study"));
		s.setEmail(rs.getString("email"));
		s.setStatus(rs.getString("status"));
		return s;
	}

	// Helper: status default & normalization
	private String normalizeStatus(String status) {
		if (status == null || status.trim().isEmpty())
			return "ACTIVE";
		String v = status.trim().toUpperCase();
		if (!v.equals("ACTIVE") && !v.equals("INACTIVE"))
			return "ACTIVE";
		return v;
	}
}
