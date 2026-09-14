package com.jobmatch.service;

import com.jobmatch.config.ScoringWeightsConfig;
import com.jobmatch.dto.RecommendationResponse;
import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import com.jobmatch.repository.CandidateRepository;
import com.jobmatch.repository.JobRepository;
import com.jobmatch.service.scoring.ScoringEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final ScoringEngine scoringEngine;

    // Candidate -> Recommended Jobs
    public List<RecommendationResponse> recommendJobsForCandidate(
            UUID candidateId, int limit, ScoringWeightsConfig weights) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        List<Job> allJobs = jobRepository.findAll();

        return allJobs.stream()
                .map(job -> scoringEngine.score(candidate, job, weights))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparingInt(RecommendationResponse::getOverallScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // BONUS — Job -> Recommended Candidates (Reverse View)
    public List<RecommendationResponse> recommendCandidatesForJob(
            UUID jobId, int limit, ScoringWeightsConfig weights) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        List<Candidate> allCandidates = candidateRepository.findAll();

        return allCandidates.stream()
                .map(candidate -> scoringEngine.score(candidate, job, weights))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparingInt(RecommendationResponse::getOverallScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
