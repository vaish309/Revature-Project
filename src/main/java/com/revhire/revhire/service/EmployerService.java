package com.revhire.revhire.service;

import com.revhire.revhire.entity.Employer;
import com.revhire.revhire.entity.Job;
import com.revhire.revhire.entity.User;
import com.revhire.revhire.exception.ResourceNotFoundException;
import com.revhire.revhire.exception.UnauthorizedException;
import com.revhire.revhire.repository.EmployerRepository;
import com.revhire.revhire.repository.JobApplicationRepository;
import com.revhire.revhire.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public Employer getEmployerProfile(User user) {
        return employerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));
    }

    @Transactional
    public Employer updateEmployerProfile(User user, Employer updatedEmployer) {
        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));

        employer.setCompanyName(updatedEmployer.getCompanyName());
        employer.setIndustry(updatedEmployer.getIndustry());
        employer.setDescription(updatedEmployer.getDescription());
        employer.setWebsite(updatedEmployer.getWebsite());
        employer.setLocation(updatedEmployer.getLocation());
        employer.setCompanySize(updatedEmployer.getCompanySize());

        employer = employerRepository.save(employer);
        log.info("Employer profile updated: {}", employer.getCompanyName());
        
        return employer;
    }

    public Map<String, Object> getDashboardStatistics(User user) {
        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can view dashboard"));

        List<Job> jobs = jobRepository.findByEmployer(employer);
        long totalJobs = jobs.size();
        long activeJobs = jobs.stream().filter(Job::isActive).count();
        long totalApplications = jobApplicationRepository.findByJobEmployerId(employer.getId()).size();
        long pendingReviews = jobApplicationRepository.findByJobEmployerId(employer.getId()).stream()
                .filter(app -> app.getStatus() == com.revhire.revhire.entity.JobApplication.ApplicationStatus.APPLIED)
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", totalJobs);
        stats.put("activeJobs", activeJobs);
        stats.put("totalApplications", totalApplications);
        stats.put("pendingReviews", pendingReviews);

        return stats;
    }
}
