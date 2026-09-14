package com.jobmatch.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import com.jobmatch.service.scoring.ExperienceScorer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceScorerTest {

    private final ExperienceScorer scorer = new ExperienceScorer();

    private Candidate candidateWithExperience(int years) {
        return Candidate.builder()
                .yearsOfExperience(years)
                .name("Bob")
                .location("Remote")
                .expectedSalary(100000)
                .build();
    }

    private Job jobWithMinExperience(int minYears) {
        return Job.builder()
                .minYearsExperience(minYears)
                .title("Dev")
                .location("Remote")
                .salaryMin(80000)
                .salaryMax(120000)
                .build();
    }

    @Test
    void meetsRequirement_returnsFullScore() {
        assertThat(scorer.score(candidateWithExperience(5), jobWithMinExperience(5), 20)).isEqualTo(20);
    }

    @Test
    void exceedsRequirement_returnsFullScore() {
        assertThat(scorer.score(candidateWithExperience(8), jobWithMinExperience(5), 20)).isEqualTo(20);
    }

    @Test
    void belowRequirement_returnsProportionalPenalizedScore() {
        // 3 of 5 years required = 60% of 20 = 12
        assertThat(scorer.score(candidateWithExperience(3), jobWithMinExperience(5), 20)).isEqualTo(12);
    }

    @Test
    void zeroMinExperience_returnsFullScore() {
        assertThat(scorer.score(candidateWithExperience(0), jobWithMinExperience(0), 20)).isEqualTo(20);
    }
}
