package com.revhire.revhire.dto;

import com.revhire.revhire.entity.JobApplication;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private JobApplication.ApplicationStatus status;
    private LocalDateTime appliedAt;
    private String coverLetter;
    private String employerComments;
    private String applicantName;
    private String applicantEmail;
}