package com.gdg.workfit.controller.enterprise;

import com.gdg.workfit.domain.SubmissionResult;
import com.gdg.workfit.dto.enterprise.EnterpriseSubmissionDto;
import com.gdg.workfit.repository.SubmissionResultRepository;
import com.gdg.workfit.repository.SubmissionScoreRepository;
import com.gdg.workfit.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "기업-지원자", description = "기업이 지원자 결과를 조회합니다.")
@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseSubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionResultRepository submissionResultRepository;
    private final SubmissionScoreRepository submissionScoreRepository;

    public EnterpriseSubmissionController(
            SubmissionService submissionService,
            SubmissionResultRepository submissionResultRepository,
            SubmissionScoreRepository submissionScoreRepository
    ) {
        this.submissionService = submissionService;
        this.submissionResultRepository = submissionResultRepository;
        this.submissionScoreRepository = submissionScoreRepository;
    }

    @Operation(summary = "지원자 결과 목록", description = "특정 공고에 지원한 지원자들의 결과 요약을 조회합니다.")
    @GetMapping("/job-posts/{jobPostId}/submissions")
    public List<EnterpriseSubmissionDto.SubmissionSummary> list(@PathVariable Long jobPostId) {
        return submissionService.findByJobPostId(jobPostId).stream()
                .map(submission -> {
                    Optional<SubmissionResult> result = submissionResultRepository.findBySubmissionId(submission.getId());
                    return new EnterpriseSubmissionDto.SubmissionSummary(
                            submission.getId(),
                            submission.getApplicantName(),
                            submission.getApplicantEmail(),
                            submission.getStatus(),
                            result.map(SubmissionResult::getTotalScore).orElse(null),
                            result.map(SubmissionResult::getPassed).orElse(null),
                            submission.getSubmittedAt()
                    );
                })
                .collect(Collectors.toList());
    }

    @Operation(summary = "지원자 결과 상세", description = "지원자의 항목별 점수와 피드백을 조회합니다.")
    @GetMapping("/submissions/{submissionId}")
    public EnterpriseSubmissionDto.SubmissionDetail get(@PathVariable Long submissionId) {
        var submission = submissionService.get(submissionId);
        var result = submissionResultRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Result not found: " + submissionId));
        var scores = submissionScoreRepository.findBySubmissionResultId(result.getId()).stream()
                .map(score -> new EnterpriseSubmissionDto.ScoreItem(
                        score.getCriteriaName(),
                        score.getScore(),
                        score.getFeedback()
                ))
                .collect(Collectors.toList());

        return new EnterpriseSubmissionDto.SubmissionDetail(
                submission.getId(),
                submission.getApplicantName(),
                submission.getApplicantEmail(),
                submission.getStatus(),
                result.getTotalScore(),
                result.getPassed(),
                result.getFeedbackSummary(),
                scores
        );
    }
}
