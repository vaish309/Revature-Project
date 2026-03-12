package com.revhire.revhire.service;

import com.revhire.revhire.entity.Job;
import com.revhire.revhire.entity.SavedJob;
import com.revhire.revhire.entity.User;
import com.revhire.revhire.exception.BadRequestException;
import com.revhire.revhire.exception.ResourceNotFoundException;
import com.revhire.revhire.repository.JobApplicationRepository;
import com.revhire.revhire.repository.JobRepository;
import com.revhire.revhire.repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSeekerService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;

    @Transactional
    public void saveJob(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (savedJobRepository.existsByUserAndJob(user, job)) {
            throw new BadRequestException("Job already saved");
        }

        SavedJob savedJob = SavedJob.builder()
                .user(user)
                .job(job)
                .build();

        savedJobRepository.save(savedJob);
        log.info("Job saved: User {} saved Job {}", user.getEmail(), job.getTitle());
    }

    @Transactional
    public void unsaveJob(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        SavedJob savedJob = savedJobRepository.findByUserAndJob(user, job)
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found"));

        savedJobRepository.delete(savedJob);
        log.info("Job unsaved: User {} unsaved Job {}", user.getEmail(), job.getTitle());
    }

    public List<Job> getSavedJobs(User user) {
        return savedJobRepository.findByUser(user).stream()
                .map(SavedJob::getJob)
                .collect(Collectors.toList());
    }
    private final JobApplicationRepository jobApplicationRepository;
    public long getRecentApplicationCount(User user) {
        if (user == null) {
            return 0;
        }
        return jobApplicationRepository.countByApplicant(user);
    }
}
