package com.revhire.revhire.repository;

import com.revhire.revhire.entity.Employer;
import com.revhire.revhire.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployer(Employer employer);
    List<Job> findByActiveTrue();
    
    @Query("SELECT j FROM Job j WHERE " +
           "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:experienceYears IS NULL OR j.experienceYears <= :experienceYears) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:postedAfter IS NULL OR j.postedDate >= :postedAfter) AND " +
           "j.active = true")
    List<Job> searchJobs(@Param("title") String title,
                         @Param("location") String location,
                         @Param("experienceYears") Integer experienceYears,
                         @Param("jobType") Job.JobType jobType,
                         @Param("postedAfter") LocalDate postedAfter);
}
