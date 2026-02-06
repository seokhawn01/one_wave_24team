package com.gdg.workfit.controller.system;

import com.gdg.workfit.dto.enterprise.PromptDto;
import com.gdg.workfit.dto.system.EvaluationDto;
import com.gdg.workfit.repository.PromptCriteriaRepository;
import com.gdg.workfit.service.EvaluationService;
import com.gdg.workfit.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "시스템-평가", description = "AI 평가 실행 및 평가 기준 조회 API입니다.")
@RestController
@RequestMapping("/api/system")
public class SystemEvaluationController {

    private final EvaluationService evaluationService;
    private final SubmissionService submissionService;
    private final PromptCriteriaRepository promptCriteriaRepository;

    public SystemEvaluationController(
            EvaluationService evaluationService,
            SubmissionService submissionService,
            PromptCriteriaRepository promptCriteriaRepository
    ) {
        this.evaluationService = evaluationService;
        this.submissionService = submissionService;
        this.promptCriteriaRepository = promptCriteriaRepository;
    }

    @Operation(
            summary = "AI 평가 실행",
            description = """
                    기업 평가원 기준으로 submissionId를 평가해 JSON으로 반환합니다.
                    - 스펙/학력/연차/자격증 언급 금지
                    - 접근 방식과 사고 구조 중심 평가
                    - 실무 적용 가능성 중심 판단
                    - 합격/불합격은 채용 가능성 기준
                    """
    )
    @PostMapping("/evaluate/{submissionId}")
    public EvaluationDto.EvaluateResponse evaluate(@PathVariable Long submissionId) {
        var submission = submissionService.get(submissionId);
        var result = evaluationService.evaluate(submission);
        EvaluationDto.Evaluation evaluation = evaluationService.buildEvaluation(
                submission.getFinalAnswer() != null ? submission.getFinalAnswer() : submission.getDraftAnswer()
        );

        int totalScore = result.getTotalScore();
        String overallResult = result.getPassed() != null && result.getPassed() ? "PASS" : "FAIL";

        List<String> strengths = List.of(
                "문제 핵심을 중심으로 접근했습니다.",
                "전달 구조가 비교적 명확합니다."
        );

        List<String> weaknesses = List.of(
                "현실 적용 시 리스크 설명이 보완되면 좋습니다.",
                "타당성을 뒷받침할 근거가 부족합니다."
        );

        String jobRole = submission.getJobPost().getTitle();

        return new EvaluationDto.EvaluateResponse(
                jobRole,
                overallResult,
                totalScore,
                evaluation,
                strengths,
                weaknesses,
                "실무 관점에서 적용 가능성을 중심으로 평가했습니다."
        );
    }

    @Operation(summary = "평가 기준 조회", description = "프롬프트 생성 시 사용된 평가 항목을 조회합니다.")
    @GetMapping("/evaluation-criteria/{promptId}")
    public PromptDto.UpdateCriteriaRequest criteria(@PathVariable Long promptId) {
        var items = promptCriteriaRepository.findByPromptId(promptId).stream()
                .map(item -> new PromptDto.CriteriaItem(item.getName(), item.getWeight()))
                .collect(Collectors.toList());
        return new PromptDto.UpdateCriteriaRequest(items);
    }
}
