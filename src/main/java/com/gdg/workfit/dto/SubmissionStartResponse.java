package com.gdg.workfit.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionStartResponse {
    private final Long submissionId;
    private final LocalDateTime startedAt;
    private final Integer timeLimitMinutes;
}
