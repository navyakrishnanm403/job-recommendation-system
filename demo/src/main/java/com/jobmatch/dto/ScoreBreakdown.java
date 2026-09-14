package com.jobmatch.dto;

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
public class ScoreBreakdown {
    private int skillScore;
    private int skillMax;
    private String skillDetail;

    private int experienceScore;
    private int experienceMax;
    private String experienceDetail;

    private int locationScore;
    private int locationMax;
    private String locationDetail;

    private int salaryScore;
    private int salaryMax;
    private String salaryDetail;
}
