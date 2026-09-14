CREATE TABLE jobs (
    id                   VARCHAR(36)  PRIMARY KEY,
    title                VARCHAR(255) NOT NULL,
    min_years_experience INT          NOT NULL DEFAULT 0,
    location             VARCHAR(255) NOT NULL,
    salary_min           INT          NOT NULL,
    salary_max           INT          NOT NULL,
    remote_allowed       TINYINT(1)   NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE job_skills (
    job_id     VARCHAR(36)  NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    skill_type VARCHAR(20)  NOT NULL,
    CONSTRAINT chk_skill_type CHECK (skill_type IN ('MUST_HAVE', 'NICE_TO_HAVE')),
    CONSTRAINT fk_job_skills
        FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
