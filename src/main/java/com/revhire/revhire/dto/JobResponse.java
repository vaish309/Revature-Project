package com.revhire.revhire.dto;

import com.revhire.revhire.entity.Job;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String requiredSkills;
    private String location;
    private String salaryRange;
    private Job.JobType jobType;
    private Integer experienceYears;
    private String educationRequirement;
    private LocalDate applicationDeadline;
    private Integer numberOfOpenings;
    private boolean active;
    private boolean filled;
    private LocalDate postedDate;
    private String companyName;
    private Long employerId;
    private Long applicationCount;
}
