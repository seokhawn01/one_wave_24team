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
    private Integer endDay;
    private String enterpriseIconUrl;
    private String enterpriseAddress;
    private String responsibilities;
    private String qualifications;
    private String difficulty;
}
