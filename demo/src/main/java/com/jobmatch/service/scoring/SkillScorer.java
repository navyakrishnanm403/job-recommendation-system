package com.jobmatch.service.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import com.jobmatch.model.SkillType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SkillScorer {

    /**
     * Returns null if candidate lacks ANY MUST_HAVE skill (hard filter).
     * Otherwise returns score between 0 and maxScore based on NICE_TO_HAVE skills.
     */
    public Integer score(Candidate candidate, Job job, int maxScore) {
        Set<String> candidateSkills = new HashSet<>(
                candidate.getSkills().stream().map(String::toLowerCase).toList()
        );

        long mustHaveTotal = job.getRequiredSkills().stream()
                .filter(s -> s.getSkillType() == SkillType.MUST_HAVE)
                .count();

        long mustHaveMatched = job.getRequiredSkills().stream()
                .filter(s -> s.getSkillType() == SkillType.MUST_HAVE)
                .filter(s -> candidateSkills.contains(s.getSkillName().toLowerCase()))
                .count();

        // HARD FILTER: if candidate missing any must-have skill -> exclude immediately
        if (mustHaveMatched < mustHaveTotal) {
            return null;
        }

        long niceToHaveTotal = job.getRequiredSkills().stream()
                .filter(s -> s.getSkillType() == SkillType.NICE_TO_HAVE)
                .count();

        long niceToHaveMatched = job.getRequiredSkills().stream()
                .filter(s -> s.getSkillType() == SkillType.NICE_TO_HAVE)
                .filter(s -> candidateSkills.contains(s.getSkillName().toLowerCase()))
                .count();

        if (niceToHaveTotal == 0) {
            return maxScore; // No nice-to-have required -> full skill score
        }

        return (int) Math.round((double) niceToHaveMatched / niceToHaveTotal * maxScore);
    }

    public String detail(Candidate candidate, Job job) {
        Set<String> candidateSkills = new HashSet<>(
                candidate.getSkills().stream().map(String::toLowerCase).toList()
        );
        long matched = job.getRequiredSkills().stream()
                .filter(s -> s.getSkillType() == SkillType.NICE_TO_HAVE)
                .filter(s -> candidateSkills.contains(s.getSkillName().toLowerCase()))
                .count();
        long total = job.getRequiredSkills().stream()
                .filter(s -> s.getSkillType() == SkillType.NICE_TO_HAVE)
                .count();
        return matched + "/" + total + " nice-to-have skills matched";
    }
}
