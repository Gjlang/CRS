package com.crs.servlet.academic;

import com.crs.model.User;
import com.crs.service.EnrolmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

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

        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String courseCode = request.getParameter("courseCode");

        try {
            int enrolmentId = enrolmentService.createEnrolmentAfterEligibility(studentId, courseCode);
            response.sendRedirect(request.getContextPath() + "/academic/enrolments?created=" + enrolmentId);
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/academic/eligibility_result.jsp").forward(request, response);
        }
    }
}
