package com.jobmatch.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import com.jobmatch.model.JobSkill;
import com.jobmatch.model.SkillType;
import com.jobmatch.service.scoring.SkillScorer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SkillScorerTest {

    private final SkillScorer scorer = new SkillScorer();

    private Candidate candidate(String... skills) {
        return Candidate.builder()
                .name("Alice")
                .skills(List.of(skills))
                .location("NYC")
                .yearsOfExperience(4)
                .expectedSalary(100000)
                .build();
    }

    private Job jobWithSkills(JobSkill... skills) {
        return Job.builder()
                .title("Software Engineer")
                .requiredSkills(List.of(skills))
                .location("NYC")
                .minYearsExperience(3)
                .salaryMin(80000)
                .salaryMax(120000)
                .build();
    }

    @Test
    void missingMustHaveSkill_returnsNull_excludingCandidate() {
        Candidate c = candidate("Python");
        Job j = jobWithSkills(new JobSkill("Java", SkillType.MUST_HAVE));

        assertThat(scorer.score(c, j, 50)).isNull();
    }

    @Test
    void allMustHaveMatched_noNiceToHave_returnsFullScore() {
        Candidate c = candidate("Java");
        Job j = jobWithSkills(new JobSkill("Java", SkillType.MUST_HAVE));

        assertThat(scorer.score(c, j, 50)).isEqualTo(50);
    }

    @Test
    void partialNiceToHaveMatched_returnsProportionalScore() {
        Candidate c = candidate("Java", "Docker");
        Job j = jobWithSkills(
                new JobSkill("Java", SkillType.MUST_HAVE),
                new JobSkill("Docker", SkillType.NICE_TO_HAVE),
                new JobSkill("Kubernetes", SkillType.NICE_TO_HAVE)
        );

        // 1 of 2 nice-to-have skills matched = 50% of 50 = 25
        assertThat(scorer.score(c, j, 50)).isEqualTo(25);
    }

    @Test
    void caseInsensitiveSkillMatching() {
        Candidate c = candidate("java");
        Job j = jobWithSkills(new JobSkill("JAVA", SkillType.MUST_HAVE));

        assertThat(scorer.score(c, j, 50)).isEqualTo(50);
    }
}
