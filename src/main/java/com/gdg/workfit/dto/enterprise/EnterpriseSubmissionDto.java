package com.gdg.workfit.dto.enterprise;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdg.workfit.domain.SubmissionStatus;
import java.time.LocalDateTime;
import java.util.List;

public final class EnterpriseSubmissionDto {

    private EnterpriseSubmissionDto() {
    }

    public record SubmissionSummary(
            Long submissionId,
            String applicantName,
            String applicantEmail,
            SubmissionStatus status,
            Integer totalScore,
            Boolean passed,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime submittedAt
    ) {
    }

    public record SubmissionDetail(
            Long submissionId,
            String applicantName,
            String applicantEmail,
            SubmissionStatus status,
            Integer totalScore,
            Boolean passed,
            String feedbackSummary,
            List<ScoreItem> scores
    ) {
    }

    public record ScoreItem(
            String criteriaName,
            Integer score,
            String feedback
    ) {
    }
}
