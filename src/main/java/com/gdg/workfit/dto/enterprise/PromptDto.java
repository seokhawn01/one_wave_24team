package com.gdg.workfit.dto.enterprise;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;

public final class PromptDto {

    private PromptDto() {
    }

    public record CreatePromptRequest(
            Long companyId,
            String companyName,
            String companyLocation,
            String companyIconUrl,
            String title,
            String content
    ) {
    }

    public record CriteriaItem(
            String name,
            Integer weight
    ) {
    }

    public record UpdateCriteriaRequest(
            List<CriteriaItem> criteria
    ) {
    }

    public record PromptResponse(
            Long id,
            Long companyId,
            String title,
            String content,
            List<CriteriaItem> criteria
    ) {
    }
}
