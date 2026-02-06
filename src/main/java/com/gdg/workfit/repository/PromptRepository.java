package com.gdg.workfit.repository;

import com.gdg.workfit.domain.Prompt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    List<Prompt> findByCompanyId(Long companyId);
}
