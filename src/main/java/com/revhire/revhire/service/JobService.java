package com.revhire.revhire.service;

import com.revhire.revhire.dto.JobRequest;
import com.revhire.revhire.dto.JobResponse;
import com.revhire.revhire.entity.Employer;
import com.revhire.revhire.entity.Job;
import com.revhire.revhire.entity.Notification;
import com.revhire.revhire.entity.User;
import com.revhire.revhire.exception.ResourceNotFoundException;
import com.revhire.revhire.exception.UnauthorizedException;
import com.revhire.revhire.repository.EmployerRepository;
import com.revhire.revhire.repository.JobApplicationRepository;
import com.revhire.revhire.repository.JobRepository;
import com.revhire.revhire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Transactional
    public JobResponse createJob(User user, JobRequest request) {
        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can create jobs"));

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredSkills(request.getRequiredSkills())
                .location(request.getLocation())
                .salaryRange(request.getSalaryRange())
                .jobType(request.getJobType())
                .experienceYears(request.getExperienceYears())
                .educationRequirement(request.getEducationRequirement())
                .applicationDeadline(request.getApplicationDeadline())
                .numberOfOpenings(request.getNumberOfOpenings())
                .employer(employer)
                .active(true)
                .build();

        job = jobRepository.save(job);
        log.info("Job created: {} by employer: {}", job.getTitle(), employer.getCompanyName());
        
        // Create notifications for all job seekers
        notifyJobSeekersAboutNewJob(job);
        
        return mapToJobResponse(job);
    }
    
    private void notifyJobSeekersAboutNewJob(Job job) {
        List<User> jobSeekers = userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.JOB_SEEKER)
                .collect(Collectors.toList());
        
        String message = String.format("New job posted: %s at %s - %s", 
                job.getTitle(), 
                job.getEmployer().getCompanyName(),
                job.getLocation());
        
        for (User jobSeeker : jobSeekers) {
            notificationService.createNotification(
                    jobSeeker, 
                    message, 
                    Notification.NotificationType.JOB_RECOMMENDATION, 
                    job.getId()
            );
        }
        
        log.info("Notifications sent to {} job seekers for new job: {}", jobSeekers.size(), job.getTitle());
    }

    @Transactional
    public JobResponse updateJob(User user, Long jobId, JobRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can update jobs"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new UnauthorizedException("You can only update your own jobs");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setLocation(request.getLocation());
        job.setSalaryRange(request.getSalaryRange());
        job.setJobType(request.getJobType());
        job.setExperienceYears(request.getExperienceYears());
        job.setEducationRequirement(request.getEducationRequirement());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setNumberOfOpenings(request.getNumberOfOpenings());

        job = jobRepository.save(job);
        log.info("Job updated: {}", job.getTitle());
        
        return mapToJobResponse(job);
    }

    public List<JobResponse> searchJobs(String title, String location, Integer experienceYears, 
                                        Job.JobType jobType, LocalDate postedAfter) {
        List<Job> jobs = jobRepository.searchJobs(title, location, experienceYears, jobType, postedAfter);
        return jobs.stream().map(this::mapToJobResponse).collect(Collectors.toList());
    }

    public List<JobResponse> getAllActiveJobs() {
        return jobRepository.findByActiveTrue().stream()
                .map(this::mapToJobResponse)
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return mapToJobResponse(job);
    }

    public List<JobResponse> getEmployerJobs(User user) {
        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can view their jobs"));
        
        return jobRepository.findByEmployer(employer).stream()
                .map(this::mapToJobResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleJobStatus(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can toggle job status"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new UnauthorizedException("You can only toggle your own jobs");
        }

        job.setActive(!job.isActive());
        jobRepository.save(job);
        log.info("Job status toggled: {} - Active: {}", job.getTitle(), job.isActive());
    }

    @Transactional
    public void markJobAsFilled(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can mark jobs as filled"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new UnauthorizedException("You can only mark your own jobs as filled");
        }

        job.setFilled(true);
        job.setActive(false);
        jobRepository.save(job);
        log.info("Job marked as filled: {}", job.getTitle());
    }

    @Transactional
    public void deleteJob(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can delete jobs"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new UnauthorizedException("You can only delete your own jobs");
        }

        jobRepository.delete(job);
        log.info("Job deleted: {}", job.getTitle());
    }

    private JobResponse mapToJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requiredSkills(job.getRequiredSkills())
                .location(job.getLocation())
                .salaryRange(job.getSalaryRange())
                .jobType(job.getJobType())
                .experienceYears(job.getExperienceYears())
                .educationRequirement(job.getEducationRequirement())
                .applicationDeadline(job.getApplicationDeadline())
                .numberOfOpenings(job.getNumberOfOpenings())
                .active(job.isActive())
                .filled(job.isFilled())
                .postedDate(job.getPostedDate())
                .companyName(job.getEmployer().getCompanyName())
                .employerId(job.getEmployer().getId())
                .applicationCount(jobApplicationRepository.countByJob(job))
                .build();
    }
}
