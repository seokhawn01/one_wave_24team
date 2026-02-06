package com.gdg.workfit.controller;

import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.domain.Submission;
import com.gdg.workfit.dto.CriterionDto;
import com.gdg.workfit.dto.SubmissionDetailResponse;
import com.gdg.workfit.dto.SubmissionItemScoreResponse;
import com.gdg.workfit.service.EvaluationService;
import com.gdg.workfit.service.PromptService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    private final EvaluationService evaluationService;
    private final PromptService promptService;

    public SystemController(EvaluationService evaluationService, PromptService promptService) {
        this.evaluationService = evaluationService;
        this.promptService = promptService;
    }

    @PostMapping("/evaluate/{submissionId}")
    public SubmissionDetailResponse evaluate(@PathVariable @NotNull Long submissionId) {
        Submission submission = evaluationService.evaluate(submissionId);
        return toDetail(submission);
    }

    @GetMapping("/evaluation-criteria/{promptId}")
    public List<CriterionDto> getCriteria(@PathVariable @NotNull Long promptId) {
        Prompt prompt = promptService.getPromptById(promptId);
        return promptService.toCriterionDtos(prompt);
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
