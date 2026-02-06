package com.gdg.workfit.controller;

import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.dto.PromptCreateRequest;
import com.gdg.workfit.dto.PromptCriteriaUpdateRequest;
import com.gdg.workfit.dto.PromptResponse;
import com.gdg.workfit.security.SecurityUtil;
import com.gdg.workfit.service.PromptService;
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
@RequestMapping("/api/enterprise/prompts")
public class EnterprisePromptController {

    private final PromptService promptService;

    public EnterprisePromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    @PostMapping
    public PromptResponse createPrompt(@Valid @RequestBody PromptCreateRequest request) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        Prompt prompt = promptService.createPrompt(enterpriseId, request);
        return toResponse(prompt);
    }

    @PutMapping("/{promptId}/criteria")
    public PromptResponse updateCriteria(
            @PathVariable Long promptId,
            @Valid @RequestBody PromptCriteriaUpdateRequest request) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        Prompt prompt = promptService.updateCriteria(enterpriseId, promptId, request);
        return toResponse(prompt);
    }

    @GetMapping
    public List<PromptResponse> getPrompts() {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        return promptService.getPrompts(enterpriseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{promptId}")
    public PromptResponse getPrompt(@PathVariable Long promptId) {
        String enterpriseId = SecurityUtil.getCurrentSubject();
        return toResponse(promptService.getPrompt(enterpriseId, promptId));
    }

    private PromptResponse toResponse(Prompt prompt) {
        return new PromptResponse(
                prompt.getId(),
                prompt.getTitle(),
                prompt.getContent(),
                prompt.getTimeLimitMinutes(),
                promptService.toCriterionDtos(prompt)
        );
    }
}
