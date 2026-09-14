package com.jobmatch.service.scoring;

import com.jobmatch.model.Candidate;
import com.jobmatch.model.Job;
import org.springframework.stereotype.Component;

@Component
public class SalaryScorer {

    public int score(Candidate candidate, Job job, int maxScore) {
        int expected = candidate.getExpectedSalary();
        int jobMin   = job.getSalaryMin();
        int jobMax   = job.getSalaryMax();

        // Candidate expects more than job can pay -> 0 score
        if (expected > jobMax) {
            return 0;
        }

        // Job minimum meets or exceeds candidate expectation -> full score
        if (jobMin >= expected) {
            return maxScore;
        }

        // Partial overlap: candidate expected is between jobMin and jobMax
        int rangeSize = jobMax - jobMin;
        if (rangeSize == 0) {
            return expected <= jobMin ? maxScore : 0;
        }

        int overlap = jobMax - expected;
        return (int) Math.round((double) overlap / rangeSize * maxScore);
    }

    public String detail(Candidate candidate, Job job) {
        int expected = candidate.getExpectedSalary();
        if (expected > job.getSalaryMax()) {
            return "candidate expects $" + expected + ", but job max budget is $" + job.getSalaryMax();
        }
        if (job.getSalaryMin() >= expected) {
            return "job range $" + job.getSalaryMin() + "-$" + job.getSalaryMax() + " comfortably meets expectation";
        }
        return "partial overlap — expected $" + expected + " within range [$" + job.getSalaryMin() + ", $" + job.getSalaryMax() + "]";
    }
}
