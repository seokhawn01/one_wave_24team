package com.gdg.workfit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptCriteriaUpdateRequest {
    @Valid
    @NotEmpty
    private List<CriterionDto> criteria;
}
