package com.revhire.revhire.dto;

import com.revhire.revhire.entity.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetailResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private JobApplication.ApplicationStatus status;
    private LocalDateTime appliedAt;
    private String coverLetter;
    private String employerComments;
    
    // Applicant Profile
    private ApplicantProfile applicant;
    
    // Resume Information
    private ResumeInfo resume;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicantProfile {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String location;
        private String currentEmploymentStatus;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResumeInfo {
        private Long id;
        private String objective;
        private String uploadedResumeUrl;
        private String uploadedResumeFileName;
        private List<EducationInfo> education;
        private List<ExperienceInfo> experience;
        private List<SkillInfo> skills;
        private List<CertificationInfo> certifications;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationInfo {
        private Long id;
        private String institution;
        private String degree;
        private String fieldOfStudy;
        private String startDate;
        private String endDate;
        private Double gpa;
        private String description;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceInfo {
        private Long id;
        private String company;
        private String position;
        private String location;
        private String startDate;
        private String endDate;
        private Boolean currentlyWorking;
        private String description;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillInfo {
        private Long id;
        private String skillName;
        private String proficiencyLevel;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CertificationInfo {
        private Long id;
        private String certificationName;
        private String issuingOrganization;
        private String issueDate;
        private String expiryDate;
        private String credentialId;
        private String credentialUrl;
    }
}
