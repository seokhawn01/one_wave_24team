package com.gdg.workfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Long promptId;
}
