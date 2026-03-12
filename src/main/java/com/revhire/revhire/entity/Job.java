package com.revhire.revhire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    private String title;

    @Column(length = 5000)
    @NotBlank(message = "Job description is required")
    private String description;

    @Column(length = 1000)
    private String requiredSkills;

    private String location;

    private String salaryRange;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Min(value = 0, message = "Experience must be non-negative")
    private Integer experienceYears;

    private String educationRequirement;

    private LocalDate applicationDeadline;

    @Min(value = 1, message = "Number of openings must be at least 1")
    private Integer numberOfOpenings;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private boolean filled = false;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        postedDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum JobType {
        FULL_TIME,
        PART_TIME,
        CONTRACT,
        INTERNSHIP,
        REMOTE
    }
}
