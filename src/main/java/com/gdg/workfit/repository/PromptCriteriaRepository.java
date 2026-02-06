package com.gdg.workfit.repository;

import com.gdg.workfit.domain.PromptCriteria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptCriteriaRepository extends JpaRepository<PromptCriteria, Long> {
    List<PromptCriteria> findByPromptId(Long promptId);
    void deleteByPromptId(Long promptId);
    long countByPromptId(Long promptId);
}
