package com.gdg.workfit.service;

import com.gdg.workfit.domain.EvaluationCriterion;
import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.dto.CriterionDto;
import com.gdg.workfit.dto.PromptCreateRequest;
import com.gdg.workfit.dto.PromptCriteriaUpdateRequest;
import com.gdg.workfit.repository.PromptRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PromptService {

    private final PromptRepository promptRepository;

    public PromptService(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    public Prompt createPrompt(String enterpriseId, PromptCreateRequest request) {
        Prompt prompt = new Prompt();
        prompt.setEnterpriseId(enterpriseId);
        prompt.setTitle(request.getTitle());
        prompt.setContent("AI Generated Prompt: " + request.getDescription());
        prompt.setTimeLimitMinutes(request.getTimeLimitMinutes() == null ? 30 : request.getTimeLimitMinutes());

        List<EvaluationCriterion> defaults = List.of(
                buildCriterion(prompt, "Problem Solving", "Approach and reasoning quality", 100),
                buildCriterion(prompt, "Communication", "Clarity and structure of the answer", 100),
                buildCriterion(prompt, "Technical Fit", "Relevance to role requirements", 100)
        );
        prompt.getCriteria().addAll(defaults);
        return promptRepository.save(prompt);
    }

    public Prompt updateCriteria(String enterpriseId, Long promptId, PromptCriteriaUpdateRequest request) {
        Prompt prompt = getPromptOwned(promptId, enterpriseId);
        prompt.getCriteria().clear();
        for (CriterionDto dto : request.getCriteria()) {
            prompt.getCriteria().add(buildCriterion(prompt, dto.getName(), dto.getDescription(), dto.getMaxScore()));
        }
        return prompt;
    }

    @Transactional(readOnly = true)
    public List<Prompt> getPrompts(String enterpriseId) {
        return promptRepository.findByEnterpriseId(enterpriseId);
    }

    @Transactional(readOnly = true)
    public Prompt getPrompt(String enterpriseId, Long promptId) {
        return getPromptOwned(promptId, enterpriseId);
    }

    @Transactional(readOnly = true)
    public Prompt getPromptById(Long promptId) {
        return promptRepository.findById(promptId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prompt not found"));
    }

    @Transactional(readOnly = true)
    public List<CriterionDto> toCriterionDtos(Prompt prompt) {
        return prompt.getCriteria().stream()
                .map(c -> {
                    CriterionDto dto = new CriterionDto();
                    dto.setName(c.getName());
                    dto.setDescription(c.getDescription());
                    dto.setMaxScore(c.getMaxScore());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Prompt getPromptOwned(Long promptId, String enterpriseId) {
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prompt not found"));
        if (!prompt.getEnterpriseId().equals(enterpriseId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Prompt not owned by enterprise");
        }
        return prompt;
    }

    private EvaluationCriterion buildCriterion(Prompt prompt, String name, String description, int maxScore) {
        EvaluationCriterion criterion = new EvaluationCriterion();
        criterion.setPrompt(prompt);
        criterion.setName(name);
        criterion.setDescription(description);
        criterion.setMaxScore(maxScore);
        return criterion;
    }
}
