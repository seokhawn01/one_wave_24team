package com.gdg.workfit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionStartRequest {
    @NotNull
    private Long jobPostId;
}
