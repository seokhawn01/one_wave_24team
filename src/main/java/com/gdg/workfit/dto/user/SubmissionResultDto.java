package com.gdg.workfit.dto.user;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public final class SubmissionResultDto {

    private SubmissionResultDto() {
    }

    @Schema(description = "평가 결과 응답")
    public record ResultResponse(
            Long submissionId,
            Boolean passed,
            Integer totalScore,
            String feedbackSummary,
            List<ScoreItem> scores,
            @Schema(description = "프론트 이동용 URL", example = "http://localhost:3000/submissions/1/result")
            String redirectUrl
    ) {
    }

    public record ScoreItem(
            String criteriaName,
            Integer score,
            String feedback
    ) {
    }
}
