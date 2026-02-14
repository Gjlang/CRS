package com.myapp.model;

public class Student {

    private String studentId;
    private String name;
    private String program;
    private int yearOfStudy;
    private String email;
    private String status; // ACTIVE / INACTIVE

    public Student() {
    }

    public Student(String studentId, String name, String program,
                   int yearOfStudy, String email, String status) {
        this.studentId = studentId;
        this.name = name;
        this.program = program;
        this.yearOfStudy = yearOfStudy;
        this.email = email;
        this.status = status;
    }

    // ===== Getters & Setters =====

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
