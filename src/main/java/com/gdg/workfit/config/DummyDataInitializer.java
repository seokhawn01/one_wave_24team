package com.gdg.workfit.config;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.JobPostStatus;
import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.repository.JobPostRepository;
import com.gdg.workfit.repository.PromptRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyDataInitializer {

    @Bean
    CommandLineRunner seedDummyData(PromptRepository promptRepository, JobPostRepository jobPostRepository) {
        return args -> {
            if (promptRepository.count() > 0 || jobPostRepository.count() > 0) {
                return;
            }

            Prompt prompt = new Prompt();
            prompt.setEnterpriseId("demo-enterprise");
            prompt.setTitle("Sample Prompt");
            prompt.setContent("Explain a recent project you shipped and the tradeoffs you made.");
            prompt.setTimeLimitMinutes(30);
            promptRepository.save(prompt);

            JobPost jobPost = new JobPost();
            jobPost.setEnterpriseId("demo-enterprise");
            jobPost.setTitle("Backend Engineer");
            jobPost.setDescription("Build APIs with Spring Boot. 3+ years experience preferred.");
            jobPost.setPrompt(prompt);
            jobPost.setStatus(JobPostStatus.OPEN);
            jobPost.setEndDay(20);
            jobPost.setEnterpriseIconUrl("https://example.com/images/demo-enterprise.png");
            jobPost.setEnterpriseAddress("123 Demo St, Suite 400, Seoul");
            jobPost.setResponsibilities("Develop and maintain REST APIs, collaborate with product and frontend teams, and improve system performance through profiling and optimization.");
            jobPost.setQualifications("3+ years of backend development experience, solid understanding of Spring Boot and JPA, and familiarity with SQL and RESTful API design.");
            jobPost.setDifficulty("MID");
            jobPostRepository.save(jobPost);
        };
    }
}
