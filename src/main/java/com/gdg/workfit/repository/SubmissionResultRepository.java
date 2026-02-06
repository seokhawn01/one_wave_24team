package com.gdg.workfit.repository;

import com.gdg.workfit.domain.SubmissionResult;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionResultRepository extends JpaRepository<SubmissionResult, Long> {
    Optional<SubmissionResult> findBySubmissionId(Long submissionId);
}
