package com.jobmatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobSkill {

    @NotBlank(message = "Skill name cannot be blank")
    @Column(name = "skill_name")
    private String skillName;

    @NotNull(message = "Skill type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type")
    private SkillType skillType;
}

