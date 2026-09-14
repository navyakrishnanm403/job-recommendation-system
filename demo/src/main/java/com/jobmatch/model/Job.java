package com.jobmatch.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    // Skills stored in the job_skills table (each row = one JobSkill)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id")
    )
    @Builder.Default
    private List<JobSkill> requiredSkills = new ArrayList<>();

    @Column(name = "min_years_experience", nullable = false)
    private int minYearsExperience;

    @Column(nullable = false)
    private String location;

    @Column(name = "salary_min", nullable = false)
    private int salaryMin;

    @Column(name = "salary_max", nullable = false)
    private int salaryMax;

    @Column(name = "remote_allowed", nullable = false)
    private boolean remoteAllowed;
}
