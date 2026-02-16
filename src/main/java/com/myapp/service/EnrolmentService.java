package com.myapp.service;

import com.myapp.dao.EnrolmentDAO;

public class EnrolmentService {

    private final EnrolmentDAO enrolmentDAO = new EnrolmentDAO();

    public int createEnrolmentAfterEligibility(String studentId, String courseCode) {

        int lastAttempt = enrolmentDAO.getMaxAttempt(studentId, courseCode);
        int nextAttempt = lastAttempt + 1;

        if (nextAttempt > 3) {
            throw new IllegalStateException("Max attempts reached (3).");
        }

        return enrolmentDAO.createPending(studentId, courseCode, nextAttempt);
    }
}
