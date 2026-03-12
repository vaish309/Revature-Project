package com.revhire.revhire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "experience")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String company;

    private String position;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Boolean currentlyWorking = false;

    @Column(length = 1000)
    private String description;
}
