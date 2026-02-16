package com.myapp.service;

import com.myapp.dao.StudentResultDAO;

public class EligibilityService {

    private final StudentResultDAO resultDAO = new StudentResultDAO();

    public EligibilityResult checkEligibility(String studentId) {

    	Double cgpa = resultDAO.calculateAverageGradePoint(studentId);

        int failedCourses = resultDAO.countFailedCourses(studentId);

        boolean ruleA = failedCourses <= 3;
        boolean ruleB = (cgpa != null) && (cgpa >= 2.0);

        boolean eligible = ruleA && ruleB;

        return new EligibilityResult(eligible, cgpa, failedCourses, ruleA, ruleB);
    }

    public static class EligibilityResult {
        public final boolean eligible;
        public final Double cgpa;
        public final int failedCourses;
        public final boolean ruleA_pass;
        public final boolean ruleB_pass;

        public EligibilityResult(boolean eligible, Double cgpa, int failedCourses,
                                 boolean ruleA_pass, boolean ruleB_pass) {
            this.eligible = eligible;
            this.cgpa = cgpa;
            this.failedCourses = failedCourses;
            this.ruleA_pass = ruleA_pass;
            this.ruleB_pass = ruleB_pass;
        }
    }
}
