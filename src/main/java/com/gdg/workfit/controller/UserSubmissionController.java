package com.gdg.workfit.controller;

import com.gdg.workfit.domain.Submission;
import com.gdg.workfit.dto.SubmissionDetailResponse;
import com.gdg.workfit.dto.SubmissionDraftRequest;
import com.gdg.workfit.dto.SubmissionItemScoreResponse;
import com.gdg.workfit.dto.SubmissionStartRequest;
import com.gdg.workfit.dto.SubmissionStartResponse;
import com.gdg.workfit.dto.SubmissionSubmitRequest;
import com.gdg.workfit.security.SecurityUtil;
import com.gdg.workfit.service.SubmissionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submissions")
public class UserSubmissionController {

    private final SubmissionService submissionService;

    public UserSubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/start")
    public SubmissionStartResponse start(@Valid @RequestBody SubmissionStartRequest request) {
        String userId = SecurityUtil.getCurrentSubject();
        Submission submission = submissionService.startSubmission(userId, request);
        Integer timeLimit = submission.getJobPost().getPrompt().getTimeLimitMinutes();
        return new SubmissionStartResponse(submission.getId(), submission.getStartedAt(), timeLimit);
    }

    @PutMapping("/{submissionId}/draft")
    public SubmissionDetailResponse saveDraft(
            @PathVariable Long submissionId,
            @Valid @RequestBody SubmissionDraftRequest request) {
        String userId = SecurityUtil.getCurrentSubject();
        Submission submission = submissionService.saveDraft(userId, submissionId, request);
        return toDetail(submission);
    }

    @PostMapping("/{submissionId}")
    public SubmissionDetailResponse submit(
            @PathVariable Long submissionId,
            @Valid @RequestBody SubmissionSubmitRequest request) {
        String userId = SecurityUtil.getCurrentSubject();
        Submission submission = submissionService.submit(userId, submissionId, request);
        return toDetail(submission);
    }

    @GetMapping("/{submissionId}/result")
    public SubmissionDetailResponse result(@PathVariable Long submissionId) {
        String userId = SecurityUtil.getCurrentSubject();
        Submission submission = submissionService.getUserSubmission(userId, submissionId);
        return toDetail(submission);
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
