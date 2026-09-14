package com.jobmatch.dto;

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
public class CandidateRequest {
    
    @NotBlank(message = "Name is required")
    private String name;

    @NotEmpty(message = "Skills list cannot be empty")
    private List<String> skills;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private int yearsOfExperience;

    @NotBlank(message = "Location is required")
    private String location;

    @Min(value = 0, message = "Expected salary cannot be negative")
    private int expectedSalary;
}
