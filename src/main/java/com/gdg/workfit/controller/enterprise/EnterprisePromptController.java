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
                    너는 WorkFit 서비스의 "기업 평가원" 역할이다.
                    WorkFit은 이력서/학력/연차/자격증을 평가하지 않으며, 문제 해결 결과물만 평가한다.
                    원칙:
                    - 스펙/배경/학벌/연차/자격증은 절대 언급하지 않는다
                    - 정답 여부보다 접근 방식과 사고 구조를 평가한다
                    - 명확하고 간결하게 평가한다
                    - 감정적 표현 없이 객관적으로 평가한다
                    - 합격/불합격은 채용 가능성 기준으로 판단한다
                    
                    입력 정보:
                    - 직무명
                    - 기업이 제시한 문제 설명
                    - 지원자가 제출한 해결안
                    
                    이 프롬프트 내용은 평가 API에서 문제 설명으로 사용됩니다.
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
