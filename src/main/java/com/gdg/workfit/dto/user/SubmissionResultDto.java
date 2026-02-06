package com.gdg.workfit.dto.user;

import java.util.List;

public final class SubmissionResultDto {

    private SubmissionResultDto() {
    }

    public record ResultResponse(
            Long submissionId,
            Boolean passed,
            Integer totalScore,
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
