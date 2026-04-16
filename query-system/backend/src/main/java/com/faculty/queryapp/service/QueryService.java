package com.faculty.queryapp.service;

import com.faculty.queryapp.model.AnswerRequest;
import com.faculty.queryapp.model.QueryRequest;
import com.faculty.queryapp.model.StudentQuery;
import com.faculty.queryapp.repository.StudentQueryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QueryService {

    private final StudentQueryRepository repository;
    private final EmailService emailService;

    @Value("${app.allowed-email-domain}")
    private String allowedEmailDomain;

    @Value("${app.teacher-pin}")
    private String teacherPin;

    public QueryService(StudentQueryRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    // ─── Add a new student query ───────────────────
    public StudentQuery addQuery(QueryRequest request) {
        String email = request.getStudentEmail().trim().toLowerCase();

        // Validate email domain
        if (!email.endsWith(allowedEmailDomain.toLowerCase())) {
            throw new IllegalArgumentException(
                "Only college email addresses ending with " + allowedEmailDomain + " are allowed."
            );
        }

        StudentQuery query = new StudentQuery(email, request.getQuestion().trim(),
                request.getSubject() != null ? request.getSubject().trim() : "General");
        return repository.save(query);
    }

    // ─── Get all queries (for teacher) ────────────
    public List<StudentQuery> getAllQueries() {
        return repository.findAllOrderBySubmittedAtDesc();
    }

    // ─── Get queries by student email (for student view) ──
    public List<StudentQuery> getQueriesByEmail(String email) {
        return repository.findByStudentEmailOrderBySubmittedAtDesc(email.trim().toLowerCase());
    }

    // ─── Answer a query (teacher only) ────────────
    public StudentQuery answerQuery(AnswerRequest request) {
        // Verify teacher PIN
        if (!teacherPin.equals(request.getTeacherPin())) {
            throw new SecurityException("Invalid teacher PIN. Access denied.");
        }

        Optional<StudentQuery> optQuery = repository.findById(request.getQueryId());
        if (optQuery.isEmpty()) {
            throw new IllegalArgumentException("Query with ID " + request.getQueryId() + " not found.");
        }

        StudentQuery query = optQuery.get();
        query.setAnswer(request.getAnswer().trim());
        query.setAnsweredAt(LocalDateTime.now());
        query.setStatus("ANSWERED");
        StudentQuery saved = repository.save(query);

        // Optional: send email notification to student
        try {
            emailService.sendAnswerNotification(saved);
        } catch (Exception e) {
            // Email failure should not block the response
            System.err.println("Email notification failed: " + e.getMessage());
        }

        return saved;
    }

    // ─── Stats for teacher dashboard ──────────────
    public long countPending() {
        return repository.countByStatus("PENDING");
    }

    public long countAnswered() {
        return repository.countByStatus("ANSWERED");
    }

    public long countTotal() {
        return repository.count();
    }

    // ─── Delete a query (teacher only) ────────────
    public void deleteQuery(Long id, String pin) {
        if (!teacherPin.equals(pin)) {
            throw new SecurityException("Invalid teacher PIN. Access denied.");
        }
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Query with ID " + id + " not found.");
        }
        repository.deleteById(id);
    }
}
