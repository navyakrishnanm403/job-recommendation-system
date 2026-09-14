package com.jobmatch.service.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import org.springframework.stereotype.Component;

@Component
public class ExperienceScorer {

    public int score(Candidate candidate, Job job, int maxScore) {
        if (job.getMinYearsExperience() == 0) {
            return maxScore;
        }

        if (candidate.getYearsOfExperience() >= job.getMinYearsExperience()) {
            return maxScore;
        }

        // Proportional penalty instead of hard exclusion
        double ratio = (double) candidate.getYearsOfExperience() / job.getMinYearsExperience();
        return (int) Math.round(ratio * maxScore);
    }

    public String detail(Candidate candidate, Job job) {
        int cYrs = candidate.getYearsOfExperience();
        int jYrs = job.getMinYearsExperience();
        if (cYrs >= jYrs) {
            return "meets requirement (" + cYrs + " yrs)";
        }
        return cYrs + " of " + jYrs + " required years (penalized, not excluded)";
    }
}
