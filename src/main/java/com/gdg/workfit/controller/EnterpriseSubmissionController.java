package com.gdg.workfit.controller;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.Submission;
import com.gdg.workfit.dto.SubmissionDetailResponse;
import com.gdg.workfit.dto.SubmissionItemScoreResponse;
import com.gdg.workfit.dto.SubmissionSummaryResponse;
import com.gdg.workfit.security.SecurityUtil;
import com.gdg.workfit.service.JobPostService;
import com.gdg.workfit.service.SubmissionService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseSubmissionController {

    private final SubmissionService submissionService;
    private final JobPostService jobPostService;

    public EnterpriseSubmissionController(SubmissionService submissionService, JobPostService jobPostService) {
        this.submissionService = submissionService;
        this.jobPostService = jobPostService;
    }

    @GetMapping("/job-posts/{jobPostId}/submissions")
    public List<SubmissionSummaryResponse> list(@PathVariable Long jobPostId) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        JobPost jobPost = jobPostService.getEnterpriseJobPost(enterpriseId, jobPostId);
        return submissionService.getJobPostSubmissions(jobPost.getId()).stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @GetMapping("/submissions/{submissionId}")
    public SubmissionDetailResponse detail(@PathVariable Long submissionId) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        Submission submission = submissionService.getSubmission(submissionId);
        if (!submission.getJobPost().getEnterpriseId().equals(enterpriseId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Submission not owned by enterprise");
        }
        return toDetail(submission);
    }

    private SubmissionSummaryResponse toSummary(Submission submission) {
        return new SubmissionSummaryResponse(
                submission.getId(),
                submission.getUserId(),
                submission.getStatus(),
                submission.getTotalScore(),
                submission.getResultStatus()
        );
    }

    private SubmissionDetailResponse toDetail(Submission submission) {
        List<SubmissionItemScoreResponse> items = submission.getItemScores().stream()
                .map(score -> new SubmissionItemScoreResponse(
                        score.getCriterionName(),
                        score.getScore(),
                        score.getFeedback()))
                .collect(Collectors.toList());
        return new SubmissionDetailResponse(
                submission.getId(),
                submission.getStatus(),
                submission.getResultStatus(),
                submission.getTotalScore(),
                submission.getFeedbackSummary(),
                items
        );
    }
}
