package com.gdg.workfit.controller;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.dto.JobPostCreateRequest;
import com.gdg.workfit.dto.JobPostResponse;
import com.gdg.workfit.dto.JobPostUpdateRequest;
import com.gdg.workfit.security.SecurityUtil;
import com.gdg.workfit.service.JobPostService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enterprise/job-posts")
public class EnterpriseJobPostController {

    private final JobPostService jobPostService;

    public EnterpriseJobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @PostMapping
    public JobPostResponse create(@Valid @RequestBody JobPostCreateRequest request) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        return toResponse(jobPostService.create(enterpriseId, request));
    }

    @GetMapping
    public List<JobPostResponse> list() {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        return jobPostService.getEnterpriseJobPosts(enterpriseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{jobPostId}")
    public JobPostResponse get(@PathVariable Long jobPostId) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        return toResponse(jobPostService.getEnterpriseJobPost(enterpriseId, jobPostId));
    }

    @PutMapping("/{jobPostId}")
    public JobPostResponse update(
            @PathVariable Long jobPostId,
            @Valid @RequestBody JobPostUpdateRequest request) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        return toResponse(jobPostService.update(enterpriseId, jobPostId, request));
    }

    @DeleteMapping("/{jobPostId}")
    public void close(@PathVariable Long jobPostId) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        jobPostService.close(enterpriseId, jobPostId);
    }

    private JobPostResponse toResponse(JobPost jobPost) {
        return new JobPostResponse(
                jobPost.getId(),
                jobPost.getTitle(),
                jobPost.getDescription(),
                jobPost.getStatus(),
                jobPost.getEndDate(),
                jobPost.getEnterpriseIconUrl(),
                jobPost.getPrompt().getId(),
                jobPost.getPrompt().getTitle(),
                jobPost.getPrompt().getTimeLimitMinutes()
        );
    }
}
