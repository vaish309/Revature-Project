package com.revhire.revhire.controller;

import com.revhire.revhire.dto.ApiResponse;
import com.revhire.revhire.entity.Employer;
import com.revhire.revhire.entity.User;
import com.revhire.revhire.service.EmployerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employer")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;

    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Employer>> getProfile(@AuthenticationPrincipal User user) {
        Employer employer = employerService.getEmployerProfile(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved successfully", employer));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Employer>> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody Employer employer) {
        Employer updated = employerService.updateEmployerProfile(user, employer);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", updated));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard(@AuthenticationPrincipal User user) {
        Map<String, Object> stats = employerService.getDashboardStatistics(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard statistics retrieved successfully", stats));
    }
}
