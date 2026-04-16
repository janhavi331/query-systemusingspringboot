package com.faculty.queryapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_queries")
public class StudentQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, length = 150)
    private String studentEmail;

    @NotBlank(message = "Question cannot be empty")
    @Size(min = 10, max = 1000, message = "Question must be between 10 and 1000 characters")
    @Column(nullable = false, length = 1000)
    private String question;

    @Column(length = 2000)
    private String answer;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column
    private LocalDateTime answeredAt;

    @Column(nullable = false)
    private String status; // "PENDING" or "ANSWERED"

    @Column(length = 200)
    private String subject; // optional: subject/topic of query

    // ─── Constructors ──────────────────────────────
    public StudentQuery() {}

    public StudentQuery(String studentEmail, String question, String subject) {
        this.studentEmail = studentEmail;
        this.question = question;
        this.subject = subject;
        this.submittedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // ─── Getters & Setters ─────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(LocalDateTime answeredAt) { this.answeredAt = answeredAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
