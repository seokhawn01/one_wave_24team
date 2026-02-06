package com.gdg.workfit.service;

import com.gdg.workfit.domain.JobPost;
import com.gdg.workfit.domain.JobPostStatus;
import com.gdg.workfit.domain.Submission;
import com.gdg.workfit.domain.SubmissionStatus;
import com.gdg.workfit.dto.SubmissionDraftRequest;
import com.gdg.workfit.dto.SubmissionStartRequest;
import com.gdg.workfit.dto.SubmissionSubmitRequest;
import com.gdg.workfit.repository.JobPostRepository;
import com.gdg.workfit.repository.SubmissionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final JobPostRepository jobPostRepository;

    public SubmissionService(SubmissionRepository submissionRepository, JobPostRepository jobPostRepository) {
        this.submissionRepository = submissionRepository;
        this.jobPostRepository = jobPostRepository;
    }

    public Submission startSubmission(String userId, SubmissionStartRequest request) {
        JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job post not found"));
        if (jobPost.getStatus() != JobPostStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job post is closed");
        }
        Submission submission = new Submission();
        submission.setJobPost(jobPost);
        submission.setUserId(userId);
        submission.setStartedAt(LocalDateTime.now());
        submission.setStatus(SubmissionStatus.DRAFT);
        return submissionRepository.save(submission);
    }

    public Submission saveDraft(String userId, Long submissionId, SubmissionDraftRequest request) {
        Submission submission = getOwnedSubmission(submissionId, userId);
        submission.setDraftAnswer(request.getAnswer());
        submission.setStatus(SubmissionStatus.DRAFT);
        return submission;
    }

    public Submission submit(String userId, Long submissionId, SubmissionSubmitRequest request) {
        Submission submission = getOwnedSubmission(submissionId, userId);
        submission.setFinalAnswer(request.getAnswer());
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setStatus(SubmissionStatus.SUBMITTED);
        return submission;
    }

    @Transactional(readOnly = true)
    public Submission getUserSubmission(String userId, Long submissionId) {
        return getOwnedSubmission(submissionId, userId);
    }

    @Transactional(readOnly = true)
    public List<Submission> getJobPostSubmissions(Long jobPostId) {
        return submissionRepository.findByJobPostId(jobPostId);
    }

    @Transactional(readOnly = true)
    public Submission getSubmission(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found"));
    }

    private Submission getOwnedSubmission(Long submissionId, String userId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found"));
        if (!submission.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Submission not owned by user");
        }
        return submission;
    }
}
