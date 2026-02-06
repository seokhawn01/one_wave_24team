package com.gdg.workfit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Min(1)
    private Integer timeLimitMinutes = 30;
}
