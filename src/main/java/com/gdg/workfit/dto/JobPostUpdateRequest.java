package com.gdg.workfit.dto;

import com.gdg.workfit.domain.JobPostStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostUpdateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private JobPostStatus status;
}
