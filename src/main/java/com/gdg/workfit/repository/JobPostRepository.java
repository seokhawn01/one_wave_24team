package com.gdg.workfit.repository;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.JobPostStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findByEnterpriseId(String enterpriseId);
    List<JobPost> findByStatus(JobPostStatus status);
}
