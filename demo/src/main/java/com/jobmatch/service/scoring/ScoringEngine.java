package com.jobmatch.service.scoring;

import com.jobmatch.config.ScoringWeightsConfig;
import com.jobmatch.dto.RecommendationResponse;
import com.jobmatch.dto.ScoreBreakdown;
import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScoringEngine {

    private final SkillScorer skillScorer;
    private final ExperienceScorer experienceScorer;
    private final LocationScorer locationScorer;
    private final SalaryScorer salaryScorer;

    /**
     * Scores candidate against a job.
     * Returns Optional.empty() if candidate is HARD FILTERED (missing a MUST_HAVE skill).
     */
    public Optional<RecommendationResponse> score(
            Candidate candidate, Job job, ScoringWeightsConfig weights) {

        // 1. Skill Score (returns null if hard filtered)
        Integer skillScore = skillScorer.score(candidate, job, weights.getSkill());
        if (skillScore == null) {
            return Optional.empty(); // Candidate excluded from recommendations!
        }

        // 2. Experience Score
        int expScore = experienceScorer.score(candidate, job, weights.getExperience());

        // 3. Location Score
        int locScore = locationScorer.score(candidate, job, weights.getLocation());

        // 4. Salary Score
        int salScore = salaryScorer.score(candidate, job, weights.getSalary());

        int totalScore = skillScore + expScore + locScore + salScore;

        ScoreBreakdown breakdown = ScoreBreakdown.builder()
                .skillScore(skillScore).skillMax(weights.getSkill())
                .skillDetail(skillScorer.detail(candidate, job))
                .experienceScore(expScore).experienceMax(weights.getExperience())
                .experienceDetail(experienceScorer.detail(candidate, job))
                .locationScore(locScore).locationMax(weights.getLocation())
                .locationDetail(locationScorer.detail(candidate, job))
                .salaryScore(salScore).salaryMax(weights.getSalary())
                .salaryDetail(salaryScorer.detail(candidate, job))
                .build();

        return Optional.of(RecommendationResponse.builder()
                .job(job)
                .overallScore(totalScore)
                .breakdown(breakdown)
                .build());
    }
}
