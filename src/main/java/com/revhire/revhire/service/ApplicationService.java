package com.revhire.revhire.service;

import com.revhire.revhire.dto.ApplicationDetailResponse;
import com.revhire.revhire.dto.ApplicationRequest;
import com.revhire.revhire.dto.ApplicationResponse;
import com.revhire.revhire.entity.*;
import com.revhire.revhire.exception.BadRequestException;
import com.revhire.revhire.exception.ResourceNotFoundException;
import com.revhire.revhire.exception.UnauthorizedException;
import com.revhire.revhire.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    private final NotificationService notificationService;
    private final ResumeRepository resumeRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final SkillRepository skillRepository;
    private final CertificationRepository certificationRepository;

    @Transactional
    public ApplicationResponse applyForJob(User user, ApplicationRequest request) {

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (applicationRepository.existsByApplicantAndJob(user, job)) {
            throw new BadRequestException("Already applied");
        }

        JobApplication application = JobApplication.builder()
                .applicant(user)
                .job(job)
                .status(JobApplication.ApplicationStatus.APPLIED)
                .build();

        applicationRepository.save(application);

        return mapToApplicationResponse(application);
    }

    public long getApplicationCount(User user) {
        return applicationRepository.countByApplicant(user);
    }

    public List<ApplicationResponse> getMyApplications(User user) {
        return applicationRepository.findByApplicant(user).stream()
                .map(this::mapToApplicationResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getJobApplications(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can view applications"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new UnauthorizedException("You can only view applications for your own jobs");
        }

        return applicationRepository.findByJob(job).stream()
                .map(this::mapToApplicationResponse)
                .collect(Collectors.toList());
    }

    public ApplicationDetailResponse getApplicationDetails(User user, Long applicationId) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can view application details"));

        if (!application.getJob().getEmployer().getId().equals(employer.getId())) {
            throw new UnauthorizedException("You can only view applications for your own jobs");
        }

        return mapToApplicationDetailResponse(application);
    }

    private ApplicationResponse mapToApplicationResponse(JobApplication application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .companyName(application.getJob().getEmployer().getCompanyName())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .coverLetter(application.getCoverLetter())
                .employerComments(application.getEmployerComments())
                .applicantName(application.getApplicant().getName())
                .applicantEmail(application.getApplicant().getEmail())
                .build();
    }

    private ApplicationDetailResponse mapToApplicationDetailResponse(JobApplication application) {
        User applicant = application.getApplicant();

        ApplicationDetailResponse.ApplicantProfile profile = ApplicationDetailResponse.ApplicantProfile.builder()
                .id(applicant.getId())
                .name(applicant.getName())
                .email(applicant.getEmail())
                .phone(applicant.getPhone())
                .location(applicant.getLocation())
                .currentEmploymentStatus(applicant.getCurrentEmploymentStatus())
                .build();

        ApplicationDetailResponse.ResumeInfo resumeInfo = null;
        Resume resume = resumeRepository.findByUser(applicant).orElse(null);

        if (resume != null) {
            List<Education> educationList = educationRepository.findByUser(applicant);
            List<Experience> experienceList = experienceRepository.findByUser(applicant);
            List<Skill> skillsList = skillRepository.findByUser(applicant);
            List<Certification> certificationsList = certificationRepository.findByUser(applicant);

            resumeInfo = ApplicationDetailResponse.ResumeInfo.builder()
                    .id(resume.getId())
                    .objective(resume.getObjective())
                    .uploadedResumeUrl(resume.getUploadedResumeUrl())
                    .uploadedResumeFileName(resume.getUploadedResumeFileName())
                    .education(educationList.stream().map(this::mapToEducationInfo).collect(Collectors.toList()))
                    .experience(experienceList.stream().map(this::mapToExperienceInfo).collect(Collectors.toList()))
                    .skills(skillsList.stream().map(this::mapToSkillInfo).collect(Collectors.toList()))
                    .certifications(certificationsList.stream().map(this::mapToCertificationInfo).collect(Collectors.toList()))
                    .build();
        }

        return ApplicationDetailResponse.builder()
                .id(application.getId())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .companyName(application.getJob().getEmployer().getCompanyName())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .coverLetter(application.getCoverLetter())
                .employerComments(application.getEmployerComments())
                .applicant(profile)
                .resume(resumeInfo)
                .build();
    }

    public boolean hasUserApplied(User user, Long jobId) {
        if (user == null || jobId == null) {
            return false;
        }
        return applicationRepository.existsByApplicantIdAndJobId(user.getId(), jobId);
    }
    public long getRecentApplicationCount(User user) {
        return applicationRepository.countByApplicant(user);
    }
    // Education, experience, skill, certification mapping
    private ApplicationDetailResponse.EducationInfo mapToEducationInfo(Education education) {
        return ApplicationDetailResponse.EducationInfo.builder()
                .id(education.getId())
                .institution(education.getInstitution())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .startDate(education.getStartDate() != null ? education.getStartDate().toString() : null)
                .endDate(education.getEndDate() != null ? education.getEndDate().toString() : null)
                .gpa(education.getGpa())
                .description(education.getDescription())
                .build();
    }

    private ApplicationDetailResponse.ExperienceInfo mapToExperienceInfo(Experience experience) {
        return ApplicationDetailResponse.ExperienceInfo.builder()
                .id(experience.getId())
                .company(experience.getCompany())
                .position(experience.getPosition())
                .location(experience.getLocation())
                .startDate(experience.getStartDate() != null ? experience.getStartDate().toString() : null)
                .endDate(experience.getEndDate() != null ? experience.getEndDate().toString() : null)
                .currentlyWorking(experience.getCurrentlyWorking())
                .description(experience.getDescription())
                .build();
    }

    private ApplicationDetailResponse.SkillInfo mapToSkillInfo(Skill skill) {
        return ApplicationDetailResponse.SkillInfo.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .proficiencyLevel(skill.getProficiencyLevel() != null ? skill.getProficiencyLevel().name() : null)
                .build();
    }

    private ApplicationDetailResponse.CertificationInfo mapToCertificationInfo(Certification certification) {
        return ApplicationDetailResponse.CertificationInfo.builder()
                .id(certification.getId())
                .certificationName(certification.getCertificationName())
                .issuingOrganization(certification.getIssuingOrganization())
                .issueDate(certification.getIssueDate() != null ? certification.getIssueDate().toString() : null)
                .expiryDate(certification.getExpiryDate() != null ? certification.getExpiryDate().toString() : null)
                .credentialId(certification.getCredentialId())
                .credentialUrl(certification.getCredentialUrl())
                .build();
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(User user, Long applicationId,
                                                       JobApplication.ApplicationStatus status, String comments) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Only employers can update application status"));

        if (!application.getJob().getEmployer().getId().equals(employer.getId())) {
            throw new UnauthorizedException("You can only update applications for your own jobs");
        }

        application.setStatus(status);
        application.setEmployerComments(comments);
        application = applicationRepository.save(application);

        notificationService.createNotification(
                application.getApplicant(),
                "Your application for " + application.getJob().getTitle() + " has been " + status.name().toLowerCase(),
                Notification.NotificationType.APPLICATION_STATUS_UPDATE,
                application.getId()
        );

        log.info("Application status updated: {} - Status: {}", applicationId, status);

        return mapToApplicationResponse(application);
    }

    @Transactional
    public void withdrawApplication(User user, Long applicationId, String reason) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getApplicant().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only withdraw your own applications");
        }

        if (application.getStatus() == JobApplication.ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Application is already withdrawn");
        }

        application.setStatus(JobApplication.ApplicationStatus.WITHDRAWN);
        application.setWithdrawalReason(reason);
        applicationRepository.save(application);

        log.info("Application withdrawn: {} by user {}", applicationId, user.getEmail());
    }
}