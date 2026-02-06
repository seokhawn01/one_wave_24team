package com.gdg.workfit.service;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.JobPostStatus;
import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.dto.enterprise.JobPostDto;
import com.gdg.workfit.repository.JobPostRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final CompanyService companyService;
    private final PromptService promptService;

    public JobPostService(
            JobPostRepository jobPostRepository,
            CompanyService companyService,
            PromptService promptService
    ) {
        this.jobPostRepository = jobPostRepository;
        this.companyService = companyService;
        this.promptService = promptService;
    }

    public JobPost create(JobPostDto.CreateJobPostRequest request) {
        var company = companyService.getOrCreate(
                request.companyId(),
                request.companyName(),
                request.companyLocation(),
                request.companyIconUrl()
        );
        Prompt prompt = promptService.get(request.promptId());
        LocalDate openDate = request.openDate() != null ? request.openDate() : LocalDate.now();
        return jobPostRepository.save(
                new JobPost(
                        company,
                        prompt,
                        request.title(),
                        request.description(),
                        request.mainTasks(),
                        request.qualifications(),
                        request.category(),
                        request.difficulty(),
                        openDate,
                        request.closeDate(),
                        request.retentionDays(),
                        request.testDurationMinutes(),
                        request.testInfo()
                )
        );
    }

    public JobPost update(Long jobPostId, JobPostDto.UpdateJobPostRequest request) {
        JobPost jobPost = get(jobPostId);
        jobPost.update(
                request.title(),
                request.description(),
                request.mainTasks(),
                request.qualifications(),
                request.category(),
                request.difficulty(),
                request.openDate(),
                request.closeDate(),
                request.retentionDays(),
                request.testDurationMinutes(),
                request.testInfo()
        );
        return jobPost;
    }

    public void close(Long jobPostId) {
        JobPost jobPost = get(jobPostId);
        jobPost.close();
    }

    public JobPost get(Long jobPostId) {
        return jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new IllegalArgumentException("JobPost not found: " + jobPostId));
    }

    public List<JobPost> findByCompanyId(Long companyId) {
        if (companyId == null) {
            return jobPostRepository.findAll();
        }
        return jobPostRepository.findByCompanyId(companyId);
    }

    public List<JobPost> findOpenForUsers() {
        return jobPostRepository.findByStatusAndCloseDateGreaterThanEqual(
                JobPostStatus.OPEN,
                LocalDate.now()
        );
    }

    public JobPostDto.JobPostResponse toResponse(JobPost jobPost) {
        return new JobPostDto.JobPostResponse(
                jobPost.getId(),
                new JobPostDto.CompanySummary(
                        jobPost.getCompany().getId(),
                        jobPost.getCompany().getName(),
                        jobPost.getCompany().getLocation(),
                        jobPost.getCompany().getIconUrl()
                ),
                new JobPostDto.PromptSummary(
                        jobPost.getPrompt().getId(),
                        jobPost.getPrompt().getTitle()
                ),
                jobPost.getTitle(),
                jobPost.getDescription(),
                jobPost.getMainTasks(),
                jobPost.getQualifications(),
                jobPost.getCategory(),
                jobPost.getDifficulty(),
                jobPost.getStatus(),
                jobPost.getOpenDate(),
                jobPost.getCloseDate(),
                jobPost.getRetentionDays(),
                jobPost.getTestDurationMinutes(),
                jobPost.getTestInfo()
        );
    }

    public int countMainTasks(Long jobPostId) {
        JobPost jobPost = get(jobPostId);
        if (jobPost.getMainTasks() == null) {
            return 0;
        }
        return jobPost.getMainTasks().size();
    }
}
