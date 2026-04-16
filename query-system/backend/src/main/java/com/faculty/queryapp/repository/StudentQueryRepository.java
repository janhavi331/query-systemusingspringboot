package com.faculty.queryapp.repository;

import com.faculty.queryapp.model.StudentQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentQueryRepository extends JpaRepository<StudentQuery, Long> {

    // Find all queries by a specific student email
    List<StudentQuery> findByStudentEmailOrderBySubmittedAtDesc(String studentEmail);

    // Find all pending queries
    List<StudentQuery> findByStatusOrderBySubmittedAtAsc(String status);

    // Count queries by status
    long countByStatus(String status);

    // Find all ordered by submission time descending
    @Query("SELECT q FROM StudentQuery q ORDER BY q.submittedAt DESC")
    List<StudentQuery> findAllOrderBySubmittedAtDesc();
}
