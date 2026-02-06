package com.gdg.workfit.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdg.workfit.domain.SubmissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "제출 상태 응답")
    public record SubmissionResponse(
            Long id,
            Long jobPostId,
            SubmissionStatus status,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startedAt,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime submittedAt,
            @Schema(description = "프론트 이동용 URL", example = "http://localhost:3000/submissions/1/submitted")
            String redirectUrl
    ) {
    }
}
