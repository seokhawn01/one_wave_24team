package com.gdg.workfit.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PromptResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final Integer timeLimitMinutes;
    private final List<CriterionDto> criteria;
}
