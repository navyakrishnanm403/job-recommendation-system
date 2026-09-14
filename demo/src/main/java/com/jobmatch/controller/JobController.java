package com.jobmatch.controller;

import com.jobmatch.config.ScoringWeightsConfig;
import com.jobmatch.dto.JobRequest;
import com.jobmatch.dto.RecommendationResponse;
import com.jobmatch.exception.ResourceNotFoundException;
import com.jobmatch.model.Job;
import com.jobmatch.repository.JobRepository;
import com.jobmatch.service.RecommendationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Validated
public class JobController {

    private final JobRepository jobRepository;
    private final RecommendationService recommendationService;
    private final ScoringWeightsConfig defaultWeights;

    @PostMapping
    public ResponseEntity<Job> create(@Valid @RequestBody JobRequest req) {
        Job job = Job.builder()
                .title(req.getTitle())
                .requiredSkills(req.getRequiredSkills())
                .minYearsExperience(req.getMinYearsExperience())
                .location(req.getLocation())
                .salaryMin(req.getSalaryMin())
                .salaryMax(req.getSalaryMax())
                .remoteAllowed(req.isRemoteAllowed())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(jobRepository.save(job));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getById(@PathVariable UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return ResponseEntity.ok(job);
    }

    // BONUS — Reverse recommendation view (candidates for job)
    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<RecommendationResponse>> recommendations(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "limit must be at least 1") int limit,
            @RequestParam(required = false) Integer skillWeight,
            @RequestParam(required = false) Integer experienceWeight,
            @RequestParam(required = false) Integer locationWeight,
            @RequestParam(required = false) Integer salaryWeight) {

        ScoringWeightsConfig weights = ScoringWeightsConfig.fromOverrides(
                skillWeight, experienceWeight, locationWeight, salaryWeight, defaultWeights);

        return ResponseEntity.ok(
                recommendationService.recommendCandidatesForJob(id, limit, weights));
    }
}
