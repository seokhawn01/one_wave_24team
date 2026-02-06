package com.gdg.workfit.controller.enterprise;

import com.gdg.workfit.dto.enterprise.PromptDto;
import com.gdg.workfit.service.PromptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "기업-프롬프트", description = "기업이 AI 채용 문제(프롬프트)를 생성하고 평가 기준을 관리합니다.")
@RestController
@RequestMapping("/api/enterprise/prompts")
public class EnterprisePromptController {

    private final PromptService promptService;

    public EnterprisePromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    @Operation(
            summary = "프롬프트 생성",
            description = """
                    너는 WorkFit 서비스의 기업 평가원 역할이다.

                    중요:
                    - 너는 사용자나 지원자를 돕는 역할이 아니다.
                    - 너는 기업을 대신해 채용 평가용 문제를 설계하는 입장이다.
                    - 사용자가 어떤 주제로 문제를 요청하더라도,
                      그것을 그대로 설명하는 문제가 아니라
                      해당 주제를 통해 지원자의 역량을 검증할 수 있는 채용 문제로 재해석해야 한다.

                    WorkFit의 핵심 원칙은 다음과 같다:
                    - 이력서, 학력, 연차, 자격증은 평가하지 않는다.
                    - 실제 업무 상황과 유사한 문제 해결 능력만 평가한다.
                    - 정답이 하나인 문제가 아니라, 사고 과정과 해결 전략을 드러내는 문제가어야 한다.
                    - 기업이 지원자의 실무 투입 가능성을 판단할 수 있어야 한다.

                    ---

                    ### 사용자의 입력 의미 해석 규칙 (매우 중요)

                    사용자가 예를 들어:
                    - 사과를 따는 법에 대해서 문제 내줘
                    너는 이를
                     사과를 따는 방법을 설명하는 문제
                     단순한 작업을 어떻게 분석개선효율화하는지 평가하기 위한 채용 문제
                    로 해석해야 한다.

                    즉, 모든 요청은
                     이 주제를 활용해 지원자의 어떤 역량을 검증할 것인가?
                    라는 관점에서 재구성한다.

                    ---

                    ### 문제 설계 시 반드시 포함할 요소

                    - 문제 배경 (실제 업무 상황처럼 서술)
                    - 지원자에게 요구하는 역할
                    - 해결 시 고려해야 할 조건/제약
                    - 평가 포인트 (기업이 보고 싶은 역량)
                    - 제한 시간 (선택)
                    - 제출물 형식

                    ---

                    ### 출력 규칙
                    - 출력은 반드시 JSON 형식만 사용한다
                    - JSON 외의 설명 문장은 절대 출력하지 않는다
                    - 문제는 지원자에게 그대로 노출되는 채용 평가 문제다

                    ---

                    ### 출력 JSON 스키마

                    {
                      "problemTitle": "",
                      "jobRole": "",
                      "problemContext": "",
                      "taskDescription": "",
                      "constraints": [
                        ""
                      ],
                      "evaluationPoints": [
                        ""
                      ],
                      "expectedDeliverables": "",
                      "timeLimitMinutes": 0
                    }

                    ---

                    이제 기업 평가원의 입장에서,
                    사용자의 요청을 채용 평가용 문제로 재해석하여 문제를 생성하라.
                    """
    )
    @PostMapping
    public PromptDto.PromptResponse create(@RequestBody PromptDto.CreatePromptRequest request) {
        return promptService.toResponse(promptService.create(request));
    }

    @Operation(summary = "평가 항목 수정", description = "프롬프트의 평가 항목과 가중치를 수정합니다.")
    @PutMapping("/{promptId}/criteria")
    public void updateCriteria(
            @PathVariable Long promptId,
            @RequestBody PromptDto.UpdateCriteriaRequest request
    ) {
        promptService.updateCriteria(promptId, request.criteria());
    }

    @Operation(summary = "프롬프트 목록", description = "기업이 만든 프롬프트 목록을 조회합니다. companyId를 주면 해당 기업만 조회됩니다.")
    @GetMapping
    public List<PromptDto.PromptResponse> list(@RequestParam(required = false) Long companyId) {
        return promptService.findByCompanyId(companyId).stream()
                .map(promptService::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "프롬프트 상세", description = "특정 프롬프트의 상세 정보를 조회합니다.")
    @GetMapping("/{promptId}")
    public PromptDto.PromptResponse get(@PathVariable Long promptId) {
        return promptService.toResponse(promptService.get(promptId));
    }

    @Operation(summary = "평가 기준 개수", description = "프롬프트에 등록된 평가 기준 개수를 반환합니다.")
    @GetMapping("/{promptId}/criteria/count")
    public long countCriteria(@PathVariable Long promptId) {
        return promptService.countCriteria(promptId);
    }
}
