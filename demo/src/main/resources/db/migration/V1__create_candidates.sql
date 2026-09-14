CREATE TABLE candidates (
    id                  VARCHAR(36)  PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    years_of_experience INT          NOT NULL DEFAULT 0,
    location            VARCHAR(255) NOT NULL,
    expected_salary     INT          NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE candidate_skills (
    candidate_id VARCHAR(36)  NOT NULL,
    skill        VARCHAR(255) NOT NULL,
    CONSTRAINT fk_candidate_skills
        FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
