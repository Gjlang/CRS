package com.myapp.servlet.academic;

import com.myapp.model.User;
import com.myapp.service.EnrolmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/academic/enrolments/create")
public class EnrolmentCreateServlet extends HttpServlet {

    private final EnrolmentService enrolmentService = new EnrolmentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"ACADEMIC".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // ✅ Your DB model uses student_id as STRING
        String studentId = request.getParameter("studentId");
        String courseCode = request.getParameter("courseCode");

        if (studentId == null || studentId.isBlank() || courseCode == null || courseCode.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing studentId or courseCode");
            return;
        }

        try {
            int enrolmentId = enrolmentService.createEnrolmentAfterEligibility(studentId, courseCode);

            response.sendRedirect(request.getContextPath()
                    + "/academic/enrolments?created=" + enrolmentId);

        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/academic/eligibility_result.jsp")
                    .forward(request, response);
        }
    }
}
