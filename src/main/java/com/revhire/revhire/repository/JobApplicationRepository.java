package com.revhire.revhire.repository;

import com.revhire.revhire.entity.Job;
import com.revhire.revhire.entity.JobApplication;
import com.revhire.revhire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByApplicant(User applicant);

    List<JobApplication> findByJob(Job job);

    Optional<JobApplication> findByApplicantAndJob(User applicant, Job job);

    boolean existsByApplicantAndJob(User applicant, Job job);

    @Query("SELECT a.job.id FROM JobApplication a WHERE a.applicant.id = :userId")
    List<Long> findJobIdsByUserId(@Param("userId") Long userId);

    boolean existsByApplicantIdAndJobId(Long applicantId, Long jobId);

    List<JobApplication> findByJobEmployerId(Long employerId);

    long countByJob(Job job);

    long countByApplicant(User user);
}