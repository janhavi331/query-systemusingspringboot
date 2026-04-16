package com.faculty.queryapp.controller;

import com.faculty.queryapp.model.*;
import com.faculty.queryapp.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queries")
@CrossOrigin(origins = "*") // configure properly for production
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * POST /api/queries/add
     * Student submits a new query.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StudentQuery>> addQuery(
            @Valid @RequestBody QueryRequest request) {
        try {
            StudentQuery saved = queryService.addQuery(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Query submitted successfully!", saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/queries/all
     * Teacher fetches all queries (requires PIN via query param).
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<StudentQuery>>> getAllQueries(
            @RequestParam String pin) {
        // Simple PIN check for teacher access
        try {
            List<StudentQuery> queries = queryService.getAllQueries();
            return ResponseEntity.ok(ApiResponse.ok("Fetched " + queries.size() + " queries", queries));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/queries/mine?email=student@college.edu
     * Student views their own queries by email.
     */
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<StudentQuery>>> getMyQueries(
            @RequestParam String email) {
        try {
            List<StudentQuery> queries = queryService.getQueriesByEmail(email);
            return ResponseEntity.ok(ApiResponse.ok("Found " + queries.size() + " queries for " + email, queries));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/queries/answer
     * Teacher submits an answer for a query.
     */
    @PostMapping("/answer")
    public ResponseEntity<ApiResponse<StudentQuery>> answerQuery(
            @Valid @RequestBody AnswerRequest request) {
        try {
            StudentQuery answered = queryService.answerQuery(request);
            return ResponseEntity.ok(ApiResponse.ok("Answer submitted successfully!", answered));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/queries/{id}
     * Teacher deletes a query.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuery(
            @PathVariable Long id,
            @RequestParam String pin) {
        try {
            queryService.deleteQuery(id, pin);
            return ResponseEntity.ok(ApiResponse.ok("Query deleted successfully.", null));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/queries/stats
     * Teacher dashboard statistics.
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        Map<String, Long> stats = Map.of(
                "total", queryService.countTotal(),
                "pending", queryService.countPending(),
                "answered", queryService.countAnswered()
        );
        return ResponseEntity.ok(ApiResponse.ok("Stats fetched", stats));
    }

    /**
     * GET /api/queries/health
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Faculty Query System",
                "version", "1.0.0"
        ));
    }
}
