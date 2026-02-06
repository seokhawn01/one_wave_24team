package com.gdg.workfit.dto.enterprise;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdg.workfit.domain.DifficultyLevel;
import com.gdg.workfit.domain.JobCategory;
import com.gdg.workfit.domain.JobPostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

public final class JobPostDto {

    private JobPostDto() {
    }

    public record CreateJobPostRequest(
            @Schema(description = "회사 ID (없으면 신규 생성)") Long companyId,
            @Schema(description = "회사명 (companyId 없을 때 필수)") String companyName,
            @Schema(description = "회사 위치") String companyLocation,
            @Schema(description = "회사 아이콘 URL") String companyIconUrl,
            @Schema(description = "프롬프트 ID") Long promptId,
            @Schema(description = "공고 제목") String title,
            @Schema(description = "공고 요약/설명") String description,
            @Schema(description = "주요 업무(배열)", example = "[\"캠페인 기획\",\"성과 리포트 작성\"]") List<String> mainTasks,
            @Schema(description = "자격 요건") String qualifications,
            @Schema(description = "채용 직군") JobCategory category,
            @Schema(description = "난이도") DifficultyLevel difficulty,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate openDate,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate closeDate,
            @Schema(description = "공고 유지 일수") Integer retentionDays,
            @Schema(description = "테스트 시간(분)", example = "60") Integer testDurationMinutes,
            @Schema(description = "테스트 안내") String testInfo
    ) {
    }

    public record UpdateJobPostRequest(
            @Schema(description = "공고 제목") String title,
            @Schema(description = "공고 요약/설명") String description,
            @Schema(description = "주요 업무(배열)", example = "[\"캠페인 기획\",\"성과 리포트 작성\"]") List<String> mainTasks,
            @Schema(description = "자격 요건") String qualifications,
            @Schema(description = "채용 직군") JobCategory category,
            @Schema(description = "난이도") DifficultyLevel difficulty,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate openDate,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate closeDate,
            @Schema(description = "공고 유지 일수") Integer retentionDays,
            @Schema(description = "테스트 시간(분)", example = "60") Integer testDurationMinutes,
            @Schema(description = "테스트 안내") String testInfo
    ) {
    }

    public record JobPostResponse(
            Long id,
            CompanySummary company,
            PromptSummary prompt,
            String title,
            String description,
            List<String> mainTasks,
            String qualifications,
            JobCategory category,
            DifficultyLevel difficulty,
            JobPostStatus status,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate openDate,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate closeDate,
            Integer retentionDays,
            Integer testDurationMinutes,
            String testInfo
    ) {
    }

    public record CompanySummary(
            Long id,
            String name,
            String location,
            String iconUrl
    ) {
    }

    public record PromptSummary(
            Long id,
            String title
    ) {
    }
}
