package com.gdg.workfit.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdg.workfit.domain.SubmissionStatus;
import java.time.LocalDateTime;

public final class SubmissionDto {

    private SubmissionDto() {
    }

    public record StartRequest(
            Long jobPostId,
            String applicantName,
            String applicantEmail
    ) {
    }

    public record DraftRequest(
            String draftAnswer
    ) {
    }

    public record SubmitRequest(
            String finalAnswer
    ) {
    }

    public record SubmissionResponse(
            Long id,
            Long jobPostId,
            SubmissionStatus status,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startedAt,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime submittedAt,
            String redirectUrl
    ) {
    }
}
