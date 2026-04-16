package com.faculty.queryapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// ─── DTO for teacher submitting an answer ─────────
public class AnswerRequest {

    @NotNull(message = "Query ID is required")
    private Long queryId;

    @NotBlank(message = "Answer cannot be empty")
    @Size(min = 3, max = 2000, message = "Answer must be 3–2000 characters")
    private String answer;

    @NotBlank(message = "Teacher PIN is required")
    private String teacherPin; // simple authentication

    public Long getQueryId() { return queryId; }
    public void setQueryId(Long queryId) { this.queryId = queryId; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getTeacherPin() { return teacherPin; }
    public void setTeacherPin(String teacherPin) { this.teacherPin = teacherPin; }
}
