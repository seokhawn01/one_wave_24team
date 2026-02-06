package com.gdg.workfit.config;

import com.gdg.workfit.domain.Company;
import com.gdg.workfit.domain.DifficultyLevel;
import com.gdg.workfit.domain.JobCategory;
import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.domain.PromptCriteria;
import com.gdg.workfit.repository.CompanyRepository;
import com.gdg.workfit.repository.JobPostRepository;
import com.gdg.workfit.repository.PromptCriteriaRepository;
import com.gdg.workfit.repository.PromptRepository;
import java.time.LocalDate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SeedDataConfig {

    @Bean
    ApplicationRunner seedData(
            @Value("${app.seed.enabled:false}") boolean enabled,
            CompanyRepository companyRepository,
            PromptRepository promptRepository,
            PromptCriteriaRepository promptCriteriaRepository,
            JobPostRepository jobPostRepository
    ) {
        return args -> {
            if (!enabled || jobPostRepository.count() > 0) {
                return;
            }

            Company company = companyRepository.save(
                    new Company("WorkFit AI", "서울 강남구", "https://example.com/company-icon.png")
            );

            Prompt prompt = promptRepository.save(
                    new Prompt(company, "실전 과제", "실무 과제를 해결하는 방식으로 지원자를 평가합니다.")
            );

            promptCriteriaRepository.save(new PromptCriteria(prompt, "문제 해결력", 40));
            promptCriteriaRepository.save(new PromptCriteria(prompt, "커뮤니케이션", 30));
            promptCriteriaRepository.save(new PromptCriteria(prompt, "실행 가능성", 30));

            jobPostRepository.save(new JobPost(
                    company,
                    prompt,
                    "사무 운영 담당자",
                    "일상 업무 자동화를 위한 개선안을 작성하세요.",
                    java.util.List.of("문서 및 일정 관리", "팀 운영 지원"),
                    "업무 문서 작성 경험, 일정/협업 도구 활용 가능",
                    JobCategory.OFFICE_ADMIN,
                    DifficultyLevel.EASY,
                    LocalDate.now(),
                    LocalDate.now().plusDays(14),
                    30,
                    60,
                    "문서 작성, 일정 정리, 커뮤니케이션 중심 과제"
            ));

            jobPostRepository.save(new JobPost(
                    company,
                    prompt,
                    "세일즈/마케팅",
                    "신규 고객 확보 캠페인 전략을 제안하세요.",
                    java.util.List.of("캠페인 기획", "성과 분석"),
                    "캠페인/콘텐츠 기획 경험",
                    JobCategory.SALES_MARKETING,
                    DifficultyLevel.MEDIUM,
                    LocalDate.now(),
                    LocalDate.now().plusDays(10),
                    30,
                    90,
                    "타겟 분석과 실행 전략 중심"
            ));

            jobPostRepository.save(new JobPost(
                    company,
                    prompt,
                    "인사(HR)",
                    "채용 온보딩 프로세스를 개선하는 방안을 제시하세요.",
                    java.util.List.of("채용/온보딩 프로세스 설계"),
                    "HR 운영 또는 채용 프로세스 경험",
                    JobCategory.HR,
                    DifficultyLevel.MEDIUM,
                    LocalDate.now(),
                    LocalDate.now().plusDays(12),
                    30,
                    60,
                    "평가 기준과 운영 설계 중심"
            ));

            jobPostRepository.save(new JobPost(
                    company,
                    prompt,
                    "재무/회계",
                    "월별 비용 최적화 방안을 분석해 제안하세요.",
                    java.util.List.of("비용 구조 분석", "개선안 제시"),
                    "기본 회계 지식 또는 데이터 분석 경험",
                    JobCategory.FINANCE,
                    DifficultyLevel.MEDIUM,
                    LocalDate.now(),
                    LocalDate.now().plusDays(15),
                    30,
                    75,
                    "데이터 기반 개선안 작성"
            ));

            jobPostRepository.save(new JobPost(
                    company,
                    prompt,
                    "IT/데이터",
                    "업무 프로세스 자동화 아이디어를 제안하세요.",
                    java.util.List.of("업무 자동화 설계", "구현 방안 제시"),
                    "기본 프로그래밍 또는 데이터 처리 경험",
                    JobCategory.IT,
                    DifficultyLevel.HARD,
                    LocalDate.now(),
                    LocalDate.now().plusDays(20),
                    30,
                    90,
                    "기술 구현 가능성 중심"
            ));
        };
    }
}
