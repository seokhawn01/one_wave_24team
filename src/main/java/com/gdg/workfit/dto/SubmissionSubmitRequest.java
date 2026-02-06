package com.gdg.workfit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionSubmitRequest {
    @NotBlank
    private String answer;
}
