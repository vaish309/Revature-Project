package com.revhire.revhire.controller;
import org.springframework.security.core.Authentication;
import com.revhire.revhire.dto.ApiResponse;
import com.revhire.revhire.dto.ApplicationRequest;
import com.revhire.revhire.dto.ApplicationResponse;
import com.revhire.revhire.entity.User;
import com.revhire.revhire.exception.BadRequestException;
import com.revhire.revhire.exception.UnauthorizedException;
import com.revhire.revhire.repository.JobApplicationRepository;
import com.revhire.revhire.repository.UserRepository;
import com.revhire.revhire.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyForJob(
            @AuthenticationPrincipal User user,
            @RequestBody ApplicationRequest request) {

        if (user == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Unauthorized. Please login.", null));
        }

        try {
            ApplicationResponse response = applicationService.applyForJob(user, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Application submitted successfully", response));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getMyApplicationCount(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long count = applicationService.getApplicationCount(user);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                applicationService.getMyApplications(user));
    }
    @GetMapping("/applied-job-ids")
    public ResponseEntity<List<Long>> getAppliedJobIds(Authentication authentication) {

        String email = authentication.getName();
        System.out.println("Logged in user: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> jobIds = applicationRepository.findJobIdsByUserId(user.getId());

        return ResponseEntity.ok(jobIds);
    }
    @GetMapping("/check/{jobId}")
    public ResponseEntity<Boolean> hasApplied(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {

        if (user == null) {
            return ResponseEntity.status(401).body(false);
        }

        boolean applied = applicationService.hasUserApplied(user, jobId);
        return ResponseEntity.ok(applied);
    }
}