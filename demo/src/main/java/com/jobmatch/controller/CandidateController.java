package com.jobmatch.controller;

import com.jobmatch.config.ScoringWeightsConfig;
import com.jobmatch.dto.CandidateRequest;
import com.jobmatch.dto.RecommendationResponse;
import com.jobmatch.exception.ResourceNotFoundException;
import com.jobmatch.model.Candidate;
import com.jobmatch.repository.CandidateRepository;
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
@RequestMapping("/candidates")
@RequiredArgsConstructor
@Validated
public class CandidateController {

    private final CandidateRepository candidateRepository;
    private final RecommendationService recommendationService;
    private final ScoringWeightsConfig defaultWeights;

    @PostMapping
    public ResponseEntity<Candidate> create(@Valid @RequestBody CandidateRequest req) {
        Candidate candidate = Candidate.builder()
                .name(req.getName())
                .skills(req.getSkills())
                .yearsOfExperience(req.getYearsOfExperience())
                .location(req.getLocation())
                .expectedSalary(req.getExpectedSalary())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateRepository.save(candidate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getById(@PathVariable UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        return ResponseEntity.ok(candidate);
    }

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
                recommendationService.recommendJobsForCandidate(id, limit, weights));
    }
}
