package com.gdg.workfit.dto;

import com.gdg.workfit.domain.ResultStatus;
import com.gdg.workfit.domain.SubmissionStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionDetailResponse {
    private final Long submissionId;
    private final SubmissionStatus status;
    private final ResultStatus resultStatus;
    private final Integer totalScore;
    private final String feedbackSummary;
    private final List<SubmissionItemScoreResponse> items;
}
