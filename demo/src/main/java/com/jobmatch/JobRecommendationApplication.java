package com.jobmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class JobRecommendationApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobRecommendationApplication.class, args);
    }
}
