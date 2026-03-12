package com.revhire.revhire.dto;

import com.revhire.revhire.entity.Job;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    
    @NotBlank(message = "Job title is required")
    private String title;
    
    @NotBlank(message = "Job description is required")
    private String description;
    
    private String requiredSkills;
    private String location;
    private String salaryRange;
    
    private Job.JobType jobType;
    
    @Min(value = 0, message = "Experience must be non-negative")
    private Integer experienceYears;
    
    private String educationRequirement;
    private LocalDate applicationDeadline;
    
    @Min(value = 1, message = "Number of openings must be at least 1")
    private Integer numberOfOpenings;
}
