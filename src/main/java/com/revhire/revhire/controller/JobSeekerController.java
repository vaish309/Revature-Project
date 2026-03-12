package com.revhire.revhire.controller;

import com.revhire.revhire.dto.ApiResponse;
import com.revhire.revhire.dto.ResumeRequest;
import com.revhire.revhire.entity.*;
import com.revhire.revhire.repository.UserRepository;
import com.revhire.revhire.service.JobSeekerService;
import com.revhire.revhire.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobseeker")
@RequiredArgsConstructor
public class JobSeekerController {

    private final ResumeService resumeService;
    private final JobSeekerService jobSeekerService;

    private final UserRepository userRepository;
    @GetMapping("/recent-applications-count")

    public long getRecentApplicationCount(java.security.Principal principal) {
        // principal.getName() usually returns the username/email
        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return dashboardService.getRecentApplicationCount(user);
    }
    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<Resume>> createOrUpdateResume(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ResumeRequest request) {
        Resume resume = resumeService.createOrUpdateResume(user, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Resume updated successfully", resume));
    }

    @PostMapping("/resume/upload")
    public ResponseEntity<ApiResponse<Resume>> uploadResume(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        Resume resume = resumeService.uploadResume(user, file);
        return ResponseEntity.ok(new ApiResponse<>(true, "Resume uploaded successfully", resume));
    }

    @GetMapping("/resume")
    public ResponseEntity<ApiResponse<Resume>> getResume(@AuthenticationPrincipal User user) {
        Resume resume = resumeService.getResume(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Resume retrieved successfully", resume));
    }

    @PostMapping("/education")
    public ResponseEntity<ApiResponse<Education>> addEducation(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody Education education) {
        Education saved = resumeService.addEducation(user, education);
        return ResponseEntity.ok(new ApiResponse<>(true, "Education added successfully", saved));
    }

    @GetMapping("/education")
    public ResponseEntity<ApiResponse<List<Education>>> getEducation(@AuthenticationPrincipal User user) {
        List<Education> education = resumeService.getEducation(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Education retrieved successfully", education));
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEducation(@PathVariable Long id) {
        resumeService.deleteEducation(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Education deleted successfully", null));
    }

    @PostMapping("/experience")
    public ResponseEntity<ApiResponse<Experience>> addExperience(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody Experience experience) {
        Experience saved = resumeService.addExperience(user, experience);
        return ResponseEntity.ok(new ApiResponse<>(true, "Experience added successfully", saved));
    }

    @GetMapping("/experience")
    public ResponseEntity<ApiResponse<List<Experience>>> getExperience(@AuthenticationPrincipal User user) {
        List<Experience> experience = resumeService.getExperience(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Experience retrieved successfully", experience));
    }

    @DeleteMapping("/experience/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable Long id) {
        resumeService.deleteExperience(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Experience deleted successfully", null));
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<Skill>> addSkill(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody Skill skill) {
        Skill saved = resumeService.addSkill(user, skill);
        return ResponseEntity.ok(new ApiResponse<>(true, "Skill added successfully", saved));
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getSkills(@AuthenticationPrincipal User user) {
        List<Skill> skills = resumeService.getSkills(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Skills retrieved successfully", skills));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable Long id) {
        resumeService.deleteSkill(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Skill deleted successfully", null));
    }

    @PostMapping("/certifications")
    public ResponseEntity<ApiResponse<Certification>> addCertification(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody Certification certification) {
        Certification saved = resumeService.addCertification(user, certification);
        return ResponseEntity.ok(new ApiResponse<>(true, "Certification added successfully", saved));
    }

    @GetMapping("/certifications")
    public ResponseEntity<ApiResponse<List<Certification>>> getCertifications(@AuthenticationPrincipal User user) {
        List<Certification> certifications = resumeService.getCertifications(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Certifications retrieved successfully", certifications));
    }

    @DeleteMapping("/certifications/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(@PathVariable Long id) {
        resumeService.deleteCertification(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Certification deleted successfully", null));
    }

    @PostMapping("/saved-jobs/{jobId}")
    public ResponseEntity<ApiResponse<Void>> saveJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        jobSeekerService.saveJob(user, jobId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job saved successfully", null));
    }

    @DeleteMapping("/saved-jobs/{jobId}")
    public ResponseEntity<ApiResponse<Void>> unsaveJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        jobSeekerService.unsaveJob(user, jobId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job unsaved successfully", null));
    }

    @GetMapping("/saved-jobs")
    public ResponseEntity<ApiResponse<List<Job>>> getSavedJobs(@AuthenticationPrincipal User user) {
        List<Job> jobs = jobSeekerService.getSavedJobs(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Saved jobs retrieved successfully", jobs));
    }
    private final JobSeekerService dashboardService;
    @GetMapping("/recent-applications/count")
    public Map<String, Long> getRecentApplicationCount(@AuthenticationPrincipal User user) {
        long count = dashboardService.getRecentApplicationCount(user);
        return Collections.singletonMap("recentApplicationCount", count);
    }
    }
