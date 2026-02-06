package com.gdg.workfit.controller.user;

import com.gdg.workfit.dto.user.SubmissionDto;
import com.gdg.workfit.dto.user.SubmissionResultDto;
import com.gdg.workfit.common.FrontendUrlBuilder;
import com.gdg.workfit.repository.SubmissionResultRepository;
import com.gdg.workfit.repository.SubmissionScoreRepository;
import com.gdg.workfit.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저-지원", description = "유저가 문제를 시작하고 제출하며 결과를 조회합니다.")
@RestController
@RequestMapping("/api/submissions")
public class UserSubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionResultRepository submissionResultRepository;
    private final SubmissionScoreRepository submissionScoreRepository;
    private final FrontendUrlBuilder frontendUrlBuilder;

    public UserSubmissionController(
            SubmissionService submissionService,
            SubmissionResultRepository submissionResultRepository,
            SubmissionScoreRepository submissionScoreRepository,
            FrontendUrlBuilder frontendUrlBuilder
    ) {
        this.submissionService = submissionService;
        this.submissionResultRepository = submissionResultRepository;
        this.submissionScoreRepository = submissionScoreRepository;
        this.frontendUrlBuilder = frontendUrlBuilder;
    }

    @Operation(summary = "문제 시작", description = "채용 공고의 문제 풀이를 시작합니다.")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = SubmissionDto.SubmissionResponse.class)))
    @PostMapping("/start")
    public SubmissionDto.SubmissionResponse start(@RequestBody SubmissionDto.StartRequest request) {
        var submission = submissionService.start(request);
        return new SubmissionDto.SubmissionResponse(
                submission.getId(),
                submission.getJobPost().getId(),
                submission.getStatus(),
                submission.getStartedAt(),
                submission.getSubmittedAt(),
                frontendUrlBuilder.submissionComplete(submission.getId())
        );
    }

    @Operation(summary = "임시 저장", description = "문제 풀이 내용을 임시 저장합니다.")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = SubmissionDto.SubmissionResponse.class)))
    @PutMapping("/{submissionId}/draft")
    public SubmissionDto.SubmissionResponse saveDraft(
            @PathVariable Long submissionId,
            @RequestBody SubmissionDto.DraftRequest request
    ) {
        var submission = submissionService.saveDraft(submissionId, request);
        return new SubmissionDto.SubmissionResponse(
                submission.getId(),
                submission.getJobPost().getId(),
                submission.getStatus(),
                submission.getStartedAt(),
                submission.getSubmittedAt(),
                frontendUrlBuilder.submissionComplete(submission.getId())
        );
    }

    @Operation(summary = "최종 제출", description = "문제 풀이를 최종 제출합니다.")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = SubmissionDto.SubmissionResponse.class)))
    @PostMapping("/{submissionId}")
    public SubmissionDto.SubmissionResponse submit(
            @PathVariable Long submissionId,
            @RequestBody SubmissionDto.SubmitRequest request
    ) {
        var submission = submissionService.submit(submissionId, request);
        return new SubmissionDto.SubmissionResponse(
                submission.getId(),
                submission.getJobPost().getId(),
                submission.getStatus(),
                submission.getStartedAt(),
                submission.getSubmittedAt(),
                frontendUrlBuilder.submissionResult(submission.getId())
        );
    }

    @Operation(summary = "결과 조회", description = "제출 결과(합/불합격, 점수, 피드백)를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = SubmissionResultDto.ResultResponse.class)))
    @GetMapping("/{submissionId}/result")
    public SubmissionResultDto.ResultResponse result(@PathVariable Long submissionId) {
        var result = submissionResultRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Result not found: " + submissionId));
        var scores = submissionScoreRepository.findBySubmissionResultId(result.getId()).stream()
                .map(score -> new SubmissionResultDto.ScoreItem(
                        score.getCriteriaName(),
                        score.getScore(),
                        score.getFeedback()
                ))
                .collect(Collectors.toList());
        return new SubmissionResultDto.ResultResponse(
                submissionId,
                result.getPassed(),
                result.getTotalScore(),
                result.getFeedbackSummary(),
                scores,
                frontendUrlBuilder.submissionResult(submissionId)
        );
    }
}
