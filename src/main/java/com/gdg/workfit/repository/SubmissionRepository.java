package com.gdg.workfit.repository;

import com.gdg.workfit.domain.Submission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByJobPostId(Long jobPostId);
}
