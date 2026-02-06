package com.gdg.workfit.service;

import com.gdg.workfit.domain.EvaluationCriterion;
import com.gdg.workfit.domain.ResultStatus;
import com.gdg.workfit.domain.Submission;
import com.gdg.workfit.domain.SubmissionScore;
import com.gdg.workfit.domain.SubmissionStatus;
import com.gdg.workfit.repository.SubmissionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class EvaluationService {

    private final SubmissionRepository submissionRepository;

    public EvaluationService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Submission evaluate(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found"));
        List<EvaluationCriterion> criteria = submission.getJobPost().getPrompt().getCriteria();

        int maxTotal = criteria.stream().mapToInt(EvaluationCriterion::getMaxScore).sum();
        int totalScore = 0;
        List<SubmissionScore> scores = new ArrayList<>();

        for (EvaluationCriterion criterion : criteria) {
            int score = Math.round(criterion.getMaxScore() * 0.7f);
            totalScore += score;
            SubmissionScore item = new SubmissionScore();
            item.setSubmission(submission);
            item.setCriterionName(criterion.getName());
            item.setScore(score);
            item.setFeedback("AI feedback for " + criterion.getName());
            scores.add(item);
        }

        submission.getItemScores().clear();
        submission.getItemScores().addAll(scores);
        submission.setTotalScore(totalScore);
        submission.setResultStatus(totalScore >= Math.round(maxTotal * 0.6f) ? ResultStatus.PASS : ResultStatus.FAIL);
        submission.setFeedbackSummary("AI summary feedback");
        submission.setStatus(SubmissionStatus.EVALUATED);
        return submission;
    }
}
