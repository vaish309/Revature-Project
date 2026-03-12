package com.revhire.revhire.controller;

import com.revhire.revhire.dto.*;
import com.revhire.revhire.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/jobseeker")
    public ResponseEntity<ApiResponse<LoginResponse>> registerJobSeeker(
            @Valid @RequestBody RegisterJobSeekerRequest request) {
        LoginResponse response = authService.registerJobSeeker(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job seeker registered successfully", response));
    }

    @PostMapping("/register/employer")
    public ResponseEntity<ApiResponse<LoginResponse>> registerEmployer(
            @Valid @RequestBody RegisterEmployerRequest request) {
        LoginResponse response = authService.registerEmployer(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employer registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }
}
