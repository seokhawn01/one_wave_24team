package com.gdg.workfit.service;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.JobPostStatus;
import com.gdg.workfit.domain.Prompt;
import com.gdg.workfit.dto.JobPostCreateRequest;
import com.gdg.workfit.dto.JobPostUpdateRequest;
import com.gdg.workfit.repository.JobPostRepository;
import com.gdg.workfit.repository.PromptRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final PromptRepository promptRepository;

    public JobPostService(JobPostRepository jobPostRepository, PromptRepository promptRepository) {
        this.jobPostRepository = jobPostRepository;
        this.promptRepository = promptRepository;
    }

    public JobPost create(String enterpriseId, JobPostCreateRequest request) {
        Prompt prompt = promptRepository.findById(request.getPromptId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prompt not found"));
        if (!prompt.getEnterpriseId().equals(enterpriseId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Prompt not owned by enterprise");
        }
        JobPost jobPost = new JobPost();
        jobPost.setEnterpriseId(enterpriseId);
        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        jobPost.setEndDate(request.getEndDate());
        jobPost.setEnterpriseIconUrl(request.getEnterpriseIconUrl());
        jobPost.setEnterpriseAddress(request.getEnterpriseAddress());
        jobPost.setResponsibilities(request.getResponsibilities());
        jobPost.setQualifications(request.getQualifications());
        jobPost.setDifficulty(request.getDifficulty());
        jobPost.setPrompt(prompt);
        jobPost.setStatus(JobPostStatus.OPEN);
        return jobPostRepository.save(jobPost);
    }

    public JobPost update(String enterpriseId, Long jobPostId, JobPostUpdateRequest request) {
        JobPost jobPost = getOwnedJobPost(jobPostId, enterpriseId);
        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        if (request.getEndDate() != null) {
            jobPost.setEndDate(request.getEndDate());
        }
        if (request.getEnterpriseIconUrl() != null) {
            jobPost.setEnterpriseIconUrl(request.getEnterpriseIconUrl());
        }
        if (request.getEnterpriseAddress() != null) {
            jobPost.setEnterpriseAddress(request.getEnterpriseAddress());
        }
        if (request.getResponsibilities() != null) {
            jobPost.setResponsibilities(request.getResponsibilities());
        }
        if (request.getQualifications() != null) {
            jobPost.setQualifications(request.getQualifications());
        }
        if (request.getDifficulty() != null) {
            jobPost.setDifficulty(request.getDifficulty());
        }
        if (request.getStatus() != null) {
            jobPost.setStatus(request.getStatus());
        }
        return jobPost;
    }

    public void close(String enterpriseId, Long jobPostId) {
        JobPost jobPost = getOwnedJobPost(jobPostId, enterpriseId);
        jobPost.setStatus(JobPostStatus.CLOSED);
    }

    @Transactional(readOnly = true)
    public List<JobPost> getEnterpriseJobPosts(String enterpriseId) {
        return jobPostRepository.findByEnterpriseId(enterpriseId);
    }

    @Transactional(readOnly = true)
    public JobPost getEnterpriseJobPost(String enterpriseId, Long jobPostId) {
        return getOwnedJobPost(jobPostId, enterpriseId);
    }

    @Transactional(readOnly = true)
    public List<JobPost> getOpenJobPosts() {
        return jobPostRepository.findByStatus(JobPostStatus.OPEN);
    }

    @Transactional(readOnly = true)
    public JobPost getOpenJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not found"));
        if (jobPost.getStatus() != JobPostStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not open");
        }
        return jobPost;
    }

    private JobPost getOwnedJobPost(Long jobPostId, String enterpriseId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not found"));
        if (!jobPost.getEnterpriseId().equals(enterpriseId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Job post not owned by enterprise");
        }
        return jobPost;
    }
}
