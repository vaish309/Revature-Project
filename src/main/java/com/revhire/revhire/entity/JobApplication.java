package com.revhire.revhire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "job_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(length = 2000)
    private String coverLetter;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(length = 1000)
    private String employerComments;

    @Column(length = 500)
    private String withdrawalReason;

    @Column(name = "status_updated_at")
    private LocalDateTime statusUpdatedAt;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        statusUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        statusUpdatedAt = LocalDateTime.now();
    }

    public enum ApplicationStatus {
        APPLIED,
        UNDER_REVIEW,
        SHORTLISTED,
        REJECTED,
        WITHDRAWN
    }
}
