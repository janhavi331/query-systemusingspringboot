package com.faculty.queryapp.service;

import com.faculty.queryapp.model.StudentQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Set to false if mail is not configured
    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.mail.teacher-name:Dr. Renuka Kajale}")
    private String teacherName;

    @Value("${spring.mail.username:noreply@college.edu}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send email notification to student when teacher answers their query.
     * Only runs if app.mail.enabled=true in application.properties
     */
    public void sendAnswerNotification(StudentQuery query) {
        if (!mailEnabled) {
            System.out.println("[EmailService] Mail disabled – skipping notification to: " + query.getStudentEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(query.getStudentEmail());
            message.setSubject("Your Query Has Been Answered – " + teacherName);
            message.setText(buildEmailBody(query));
            mailSender.send(message);
            System.out.println("[EmailService] Notification sent to: " + query.getStudentEmail());
        } catch (Exception e) {
            System.err.println("[EmailService] Failed to send to " + query.getStudentEmail() + ": " + e.getMessage());
            throw e;
        }
    }

    private String buildEmailBody(StudentQuery query) {
        return """
                Dear Student,

                Your query has been answered by %s.

                ─────────────────────────────────────────
                Your Question:
                %s

                Teacher's Answer:
                %s
                ─────────────────────────────────────────

                Please log in to the faculty portal to view the full details.

                Best regards,
                %s
                Department of Computer Science & Engineering
                NMIET, Pune
                """.formatted(teacherName, query.getQuestion(), query.getAnswer(), teacherName);
    }
}
