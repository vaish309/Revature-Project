package com.revhire.revhire.controller;

import com.revhire.revhire.dto.ApiResponse;
import com.revhire.revhire.dto.JobRequest;
import com.revhire.revhire.dto.JobResponse;
import com.revhire.revhire.entity.Job;
import com.revhire.revhire.entity.User;
import com.revhire.revhire.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody JobRequest request) {
        JobResponse response = jobService.createJob(user, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job created successfully", response));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId,
            @Valid @RequestBody JobRequest request) {
        JobResponse response = jobService.updateJob(user, jobId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job updated successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobResponse>>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer experienceYears,
            @RequestParam(required = false) Job.JobType jobType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate postedAfter) {
        List<JobResponse> jobs = jobService.searchJobs(title, location, experienceYears, jobType, postedAfter);
        return ResponseEntity.ok(new ApiResponse<>(true, "Jobs retrieved successfully", jobs));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllActiveJobs() {
        List<JobResponse> jobs = jobService.getAllActiveJobs();
        return ResponseEntity.ok(new ApiResponse<>(true, "Active jobs retrieved successfully", jobs));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long jobId) {
        JobResponse job = jobService.getJobById(jobId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job retrieved successfully", job));
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getMyJobs(@AuthenticationPrincipal User user) {
        List<JobResponse> jobs = jobService.getEmployerJobs(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Your jobs retrieved successfully", jobs));
    }

    @PatchMapping("/{jobId}/toggle-status")
    public ResponseEntity<ApiResponse<Void>> toggleJobStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        jobService.toggleJobStatus(user, jobId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job status toggled successfully", null));
    }

    @PatchMapping("/{jobId}/mark-filled")
    public ResponseEntity<ApiResponse<Void>> markJobAsFilled(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        jobService.markJobAsFilled(user, jobId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job marked as filled successfully", null));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        jobService.deleteJob(user, jobId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job deleted successfully", null));
    }
}
