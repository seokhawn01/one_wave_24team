package com.gdg.workfit.service;

import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.domain.PromptCriteria;
import com.gdg.workfit.dto.enterprise.PromptDto;
import com.gdg.workfit.repository.PromptCriteriaRepository;
import com.gdg.workfit.repository.PromptRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PromptService {

    private final PromptRepository promptRepository;
    private final PromptCriteriaRepository promptCriteriaRepository;
    private final CompanyService companyService;

    public PromptService(
            PromptRepository promptRepository,
            PromptCriteriaRepository promptCriteriaRepository,
            CompanyService companyService
    ) {
        this.promptRepository = promptRepository;
        this.promptCriteriaRepository = promptCriteriaRepository;
        this.companyService = companyService;
    }

    public Prompt create(PromptDto.CreatePromptRequest request) {
        var company = companyService.getOrCreate(
                request.companyId(),
                request.companyName(),
                request.companyLocation(),
                request.companyIconUrl()
        );
        return promptRepository.save(new Prompt(company, request.title(), request.content()));
    }

    public PromptDto.PromptResponse toResponse(Prompt prompt) {
        List<PromptCriteria> criteria = promptCriteriaRepository.findByPromptId(prompt.getId());
        List<PromptDto.CriteriaItem> items = new ArrayList<>();
        for (PromptCriteria item : criteria) {
            items.add(new PromptDto.CriteriaItem(item.getName(), item.getWeight()));
        }
        return new PromptDto.PromptResponse(
                prompt.getId(),
                prompt.getCompany().getId(),
                prompt.getTitle(),
                prompt.getContent(),
                items
        );
    }

    public List<Prompt> findByCompanyId(Long companyId) {
        if (companyId == null) {
            return promptRepository.findAll();
        }
        return promptRepository.findByCompanyId(companyId);
    }

    public Prompt get(Long promptId) {
        return promptRepository.findById(promptId)
                .orElseThrow(() -> new IllegalArgumentException("Prompt not found: " + promptId));
    }

    public void updateCriteria(Long promptId, List<PromptDto.CriteriaItem> items) {
        Prompt prompt = get(promptId);
        promptCriteriaRepository.deleteByPromptId(promptId);
        if (items == null) {
            return;
        }
        for (PromptDto.CriteriaItem item : items) {
            promptCriteriaRepository.save(new PromptCriteria(prompt, item.name(), item.weight()));
        }
    }

    public long countCriteria(Long promptId) {
        return promptCriteriaRepository.countByPromptId(promptId);
    }
}
