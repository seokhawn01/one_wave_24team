package com.gdg.workfit.dto;

import com.gdg.workfit.domain.JobPostStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobPostResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final JobPostStatus status;
    private final LocalDate endDate;
    private final String enterpriseIconUrl;
    private final Long promptId;
    private final String promptTitle;
    private final Integer timeLimitMinutes;
}
