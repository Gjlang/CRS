package com.myapp.servlet.academic;

import com.myapp.dao.StudentDAO;
import com.myapp.dao.StudentResultDAO;
import com.myapp.model.Student;
import com.myapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/academic/eligibility")
public class AcademicEligibilityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final int MAX_FAILED = 3;
    private static final double DEFAULT_CGPA_THRESHOLD = 2.00;

    private final StudentDAO studentDAO = new StudentDAO();
    private final StudentResultDAO resultDAO = new StudentResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Security: Academic only
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"ACADEMIC".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Security: Academic only
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"ACADEMIC".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String studentId = req.getParameter("student_id");
        if (studentId == null || studentId.trim().isEmpty()) {
            req.setAttribute("error", "Please enter Student ID.");
            req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
            return;
        }
        studentId = studentId.trim();

        Student student = studentDAO.findById(studentId);
        if (student == null) {
            req.setAttribute("error", "Student not found: " + studentId);
            req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
            return;
        }

        int failedCount = resultDAO.countFailedCourses(studentId);
        Double avgGpa = resultDAO.calculateAverageGradePoint(studentId); // may be null

        boolean ruleA = failedCount <= MAX_FAILED;
        boolean ruleB;
        String cgpaText;

        if (avgGpa == null) {
            ruleB = true; // CGPA not available => ignore Rule B
            cgpaText = "N/A";
        } else {
            ruleB = avgGpa >= DEFAULT_CGPA_THRESHOLD;
            cgpaText = String.format("%.2f", avgGpa);
        }

        boolean eligible = ruleA && ruleB;

        StringBuilder explanation = new StringBuilder();
        explanation.append("Eligibility Rules:\n");
        explanation.append("- Rule A: Failed courses <= ").append(MAX_FAILED).append("\n");
        explanation.append("- Rule B: CGPA >= ").append(String.format("%.2f", DEFAULT_CGPA_THRESHOLD)).append(" (if CGPA available)\n\n");
        explanation.append("Computed:\n");
        explanation.append("- Failed courses = ").append(failedCount).append(" => Rule A: ").append(ruleA ? "PASS" : "FAIL").append("\n");
        explanation.append("- CGPA/GPA = ").append(cgpaText).append(" => Rule B: ").append(avgGpa == null ? "N/A (ignored)" : (ruleB ? "PASS" : "FAIL")).append("\n");

        req.setAttribute("student", student);
        req.setAttribute("failedCount", failedCount);
        req.setAttribute("cgpaText", cgpaText);
        req.setAttribute("eligible", eligible);
        req.setAttribute("explanation", explanation.toString());
        req.setAttribute("searchedStudentId", studentId);

        req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
    }
}
