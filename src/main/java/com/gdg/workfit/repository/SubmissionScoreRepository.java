package com.gdg.workfit.repository;

import com.gdg.workfit.domain.SubmissionScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionScoreRepository extends JpaRepository<SubmissionScore, Long> {
    List<SubmissionScore> findBySubmissionResultId(Long submissionResultId);
}
