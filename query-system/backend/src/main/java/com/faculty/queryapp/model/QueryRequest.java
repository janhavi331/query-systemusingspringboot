package com.faculty.queryapp.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ─── DTO for submitting a new query ───────────────
public class QueryRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String studentEmail;

    @NotBlank(message = "Question is required")
    @Size(min = 10, max = 1000, message = "Question must be 10–1000 characters")
    private String question;

    private String subject; // optional

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
