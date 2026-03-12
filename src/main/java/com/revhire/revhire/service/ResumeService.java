package com.revhire.revhire.service;

import com.revhire.revhire.dto.ResumeRequest;
import com.revhire.revhire.entity.*;
import com.revhire.revhire.exception.ResourceNotFoundException;
import com.revhire.revhire.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final SkillRepository skillRepository;
    private final CertificationRepository certificationRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public Resume createOrUpdateResume(User user, ResumeRequest request) {
        Resume resume = resumeRepository.findByUser(user)
                .orElse(Resume.builder().user(user).build());
        
        resume.setObjective(request.getObjective());
        return resumeRepository.save(resume);
    }

    @Transactional
    public Resume uploadResume(User user, MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        
        Resume resume = resumeRepository.findByUser(user)
                .orElse(Resume.builder().user(user).build());
        
        if (resume.getUploadedResumeUrl() != null) {
            fileStorageService.deleteFile(resume.getUploadedResumeUrl());
        }
        
        resume.setUploadedResumeUrl(fileName);
        resume.setUploadedResumeFileName(file.getOriginalFilename());
        
        return resumeRepository.save(resume);
    }

    public Resume getResume(User user) {
        return resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
    }

    @Transactional
    public Education addEducation(User user, Education education) {
        education.setUser(user);
        return educationRepository.save(education);
    }

    public List<Education> getEducation(User user) {
        return educationRepository.findByUser(user);
    }

    @Transactional
    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }

    @Transactional
    public Experience addExperience(User user, Experience experience) {
        experience.setUser(user);
        return experienceRepository.save(experience);
    }

    public List<Experience> getExperience(User user) {
        return experienceRepository.findByUser(user);
    }

    @Transactional
    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    @Transactional
    public Skill addSkill(User user, Skill skill) {
        skill.setUser(user);
        return skillRepository.save(skill);
    }

    public List<Skill> getSkills(User user) {
        return skillRepository.findByUser(user);
    }

    @Transactional
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    @Transactional
    public Certification addCertification(User user, Certification certification) {
        certification.setUser(user);
        return certificationRepository.save(certification);
    }

    public List<Certification> getCertifications(User user) {
        return certificationRepository.findByUser(user);
    }

    @Transactional
    public void deleteCertification(Long id) {
        certificationRepository.deleteById(id);
    }
}
