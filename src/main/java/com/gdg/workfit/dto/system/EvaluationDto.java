package com.gdg.workfit.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public final class EvaluationDto {

    private EvaluationDto() {
    }

    public record EvaluateResponse(
            String jobRole,
            String overallResult,
            Integer totalScore,
            Evaluation evaluation,
            List<String> strengths,
            List<String> weaknesses,
            String finalComment
    ) {
    }

    public record Evaluation(
            Item problemUnderstanding,
            Item solutionLogic,
            Item technicalFeasibility,
            Item practicalApplicability,
            Item communication
    ) {
    }

    public record Item(
            Integer score,
            String comment
    ) {
    }
}
