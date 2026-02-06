package com.gdg.workfit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionDraftRequest {
    @NotBlank
    private String answer;
}
