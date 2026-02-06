package com.gdg.workfit.config;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.JobPostStatus;
import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.repository.JobPostRepository;
import com.gdg.workfit.repository.PromptRepository;
import java.time.LocalDate;
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
            jobPost.setEndDate(LocalDate.now().plusDays(14));
            jobPost.setEnterpriseIconUrl("https://example.com/images/demo-enterprise.png");
            jobPostRepository.save(jobPost);
        };
    }
}
