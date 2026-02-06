package com.gdg.workfit.dto;

import com.gdg.workfit.domain.ResultStatus;
import com.gdg.workfit.domain.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionSummaryResponse {
    private final Long submissionId;
    private final String userId;
    private final SubmissionStatus status;
    private final Integer totalScore;
    private final ResultStatus resultStatus;
}
