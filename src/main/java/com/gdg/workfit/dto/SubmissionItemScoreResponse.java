package com.gdg.workfit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionItemScoreResponse {
    private final String criterionName;
    private final Integer score;
    private final String feedback;
}
