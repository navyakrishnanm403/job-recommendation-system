package com.jobmatch.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "scoring.weights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoringWeightsConfig {
    private int skill = 50;
    private int experience = 20;
    private int location = 15;
    private int salary = 15;

    public static ScoringWeightsConfig fromOverrides(
            Integer skillOverride,
            Integer experienceOverride,
            Integer locationOverride,
            Integer salaryOverride,
            ScoringWeightsConfig defaults) {
        
        ScoringWeightsConfig config = new ScoringWeightsConfig();
        config.setSkill(skillOverride != null ? skillOverride : defaults.getSkill());
        config.setExperience(experienceOverride != null ? experienceOverride : defaults.getExperience());
        config.setLocation(locationOverride != null ? locationOverride : defaults.getLocation());
        config.setSalary(salaryOverride != null ? salaryOverride : defaults.getSalary());
        return config;
    }
}
