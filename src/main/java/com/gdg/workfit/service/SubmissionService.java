package com.gdg.workfit.service;

import com.gdg.workfit.domain.Submission;
import com.gdg.workfit.dto.user.SubmissionDto;
import com.gdg.workfit.repository.SubmissionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final JobPostService jobPostService;

    public SubmissionService(SubmissionRepository submissionRepository, JobPostService jobPostService) {
        this.submissionRepository = submissionRepository;
        this.jobPostService = jobPostService;
    }

    public Submission start(SubmissionDto.StartRequest request) {
        var jobPost = jobPostService.get(request.jobPostId());
        return submissionRepository.save(new Submission(jobPost, request.applicantName(), request.applicantEmail()));
    }

    public Submission saveDraft(Long submissionId, SubmissionDto.DraftRequest request) {
        Submission submission = get(submissionId);
        submission.saveDraft(request.draftAnswer());
        return submission;
    }

    public Submission submit(Long submissionId, SubmissionDto.SubmitRequest request) {
        Submission submission = get(submissionId);
        submission.submit(request.finalAnswer());
        return submission;
    }

    public Submission get(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));
    }

    public List<Submission> findByJobPostId(Long jobPostId) {
        return submissionRepository.findByJobPostId(jobPostId);
    }

    public SubmissionDto.SubmissionResponse toResponse(Submission submission) {
        return new SubmissionDto.SubmissionResponse(
                submission.getId(),
                submission.getJobPost().getId(),
                submission.getStatus(),
                submission.getStartedAt(),
                submission.getSubmittedAt(),
                null
        );
    }
}
