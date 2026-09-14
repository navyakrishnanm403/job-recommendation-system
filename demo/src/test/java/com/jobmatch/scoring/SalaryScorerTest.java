package com.jobmatch.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import com.jobmatch.service.scoring.SalaryScorer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SalaryScorerTest {

    private final SalaryScorer scorer = new SalaryScorer();

    private Candidate candidateExpecting(int salary) {
        return Candidate.builder()
                .expectedSalary(salary)
                .name("Charlie")
                .location("NYC")
                .yearsOfExperience(3)
                .build();
    }

    private Job jobWithSalaryRange(int min, int max) {
        return Job.builder()
                .salaryMin(min)
                .salaryMax(max)
                .title("Dev")
                .location("NYC")
                .build();
    }

    @Test
    void candidateExpectsMoreThanJobMax_returnsZeroScore() {
        assertThat(scorer.score(candidateExpecting(150000), jobWithSalaryRange(80000, 120000), 15))
                .isEqualTo(0);
    }

    @Test
    void jobMinExceedsCandidateExpectation_returnsFullScore() {
        assertThat(scorer.score(candidateExpecting(80000), jobWithSalaryRange(90000, 120000), 15))
                .isEqualTo(15);
    }

    @Test
    void candidateExpectationWithinRange_returnsProportionalScore() {
        // Expected: 100k, Range: 80k-120k -> overlap = 20k, range = 40k -> 50% of 15 = 8
        int score = scorer.score(candidateExpecting(100000), jobWithSalaryRange(80000, 120000), 15);
        assertThat(score).isEqualTo(8);
    }
}
