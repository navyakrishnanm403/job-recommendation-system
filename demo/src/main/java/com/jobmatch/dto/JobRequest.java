package com.jobmatch.dto;

import com.jobmatch.model.JobSkill;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotEmpty(message = "Required skills list cannot be empty")
    private List<@Valid JobSkill> requiredSkills;

    @Min(value = 0, message = "Min years of experience cannot be negative")
    private int minYearsExperience;

    @NotBlank(message = "Location is required")
    private String location;

    @Min(value = 0, message = "Min salary cannot be negative")
    private int salaryMin;

    @Min(value = 0, message = "Max salary cannot be negative")
    private int salaryMax;

    private boolean remoteAllowed;

    @AssertTrue(message = "salaryMax must be greater than or equal to salaryMin")
    public boolean isSalaryRangeValid() {
        return salaryMax >= salaryMin;
    }
}

