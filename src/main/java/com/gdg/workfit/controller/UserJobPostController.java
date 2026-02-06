package com.gdg.workfit.controller;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.dto.JobPostResponse;
import com.gdg.workfit.service.JobPostService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-posts")
public class UserJobPostController {

    private final JobPostService jobPostService;

    public UserJobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @GetMapping
    public List<JobPostResponse> list() {
        return jobPostService.getOpenJobPosts().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{jobPostId}")
    public JobPostResponse get(@PathVariable Long jobPostId) {
        return toResponse(jobPostService.getOpenJobPost(jobPostId));
    }

    private JobPostResponse toResponse(JobPost jobPost) {
        return new JobPostResponse(
                jobPost.getId(),
                jobPost.getTitle(),
                jobPost.getDescription(),
                jobPost.getStatus(),
                jobPost.getEndDay(),
                jobPost.getEnterpriseIconUrl(),
                jobPost.getEnterpriseAddress(),
                jobPost.getResponsibilities(),
                jobPost.getQualifications(),
                jobPost.getDifficulty(),
                jobPost.getPrompt().getId(),
                jobPost.getPrompt().getTitle(),
                jobPost.getPrompt().getTimeLimitMinutes()
        );
    }
}
