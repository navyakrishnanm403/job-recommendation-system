package com.jobmatch.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import com.jobmatch.service.scoring.LocationScorer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocationScorerTest {

    private final LocationScorer scorer = new LocationScorer();

    private Candidate candidateIn(String location) {
        return Candidate.builder()
                .location(location)
                .name("Dana")
                .yearsOfExperience(3)
                .expectedSalary(95000)
                .build();
    }

    private Job jobIn(String location, boolean remoteAllowed) {
        return Job.builder()
                .title("Software Engineer")
                .location(location)
                .remoteAllowed(remoteAllowed)
                .salaryMin(80000)
                .salaryMax(120000)
                .build();
    }

    @Test
    void exactLocationMatch_returnsFullScore() {
        Candidate c = candidateIn("San Francisco");
        Job j = jobIn("San Francisco", false);

        assertThat(scorer.score(c, j, 15)).isEqualTo(15);
    }

    @Test
    void caseInsensitiveLocationMatch_returnsFullScore() {
        Candidate c = candidateIn("new york");
        Job j = jobIn("NEW YORK", false);

        assertThat(scorer.score(c, j, 15)).isEqualTo(15);
    }

    @Test
    void locationMismatchWithRemoteAllowed_returnsPartialScore() {
        Candidate c = candidateIn("Austin");
        Job j = jobIn("Seattle", true);

        // 15 * 0.67 = 10.05 -> round = 10
        assertThat(scorer.score(c, j, 15)).isEqualTo(10);
    }

    @Test
    void locationMismatchWithRemoteNotAllowed_returnsZeroScore() {
        Candidate c = candidateIn("Austin");
        Job j = jobIn("Seattle", false);

        assertThat(scorer.score(c, j, 15)).isEqualTo(0);
    }
}
