package com.jobmatch.dto;

import com.jobmatch.model.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
    private Job job;
    private int overallScore;
    private ScoreBreakdown breakdown;
}
