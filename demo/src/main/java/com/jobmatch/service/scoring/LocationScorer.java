package com.jobmatch.service.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import org.springframework.stereotype.Component;

@Component
public class LocationScorer {

    public int score(Candidate candidate, Job job, int maxScore) {
        if (candidate.getLocation().equalsIgnoreCase(job.getLocation())) {
            return maxScore;                          // Exact match
        }
        if (job.isRemoteAllowed()) {
            return (int) Math.round(maxScore * 0.67); // Partial score for remote option
        }
        return 0;                                     // Hard mismatch
    }

    public String detail(Candidate candidate, Job job) {
        if (candidate.getLocation().equalsIgnoreCase(job.getLocation())) {
            return "exact location match (" + candidate.getLocation() + ")";
        }
        if (job.isRemoteAllowed()) {
            return "location mismatch, but remote allowed";
        }
        return "location mismatch, remote not allowed";
    }
}
