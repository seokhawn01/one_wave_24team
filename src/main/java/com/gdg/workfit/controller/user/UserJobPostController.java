package com.gdg.workfit.controller.user;

import com.gdg.workfit.dto.enterprise.JobPostDto;
import com.gdg.workfit.service.JobPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저-공고", description = "유저가 채용 공고를 조회합니다.")
@RestController
@RequestMapping("/api/job-posts")
public class UserJobPostController {

    private final JobPostService jobPostService;

    public UserJobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @Operation(summary = "공고 목록", description = "현재 공개 중인 채용 공고 목록을 조회합니다.")
    @GetMapping
    public List<JobPostDto.JobPostResponse> list() {
        return jobPostService.findOpenForUsers().stream()
                .map(jobPostService::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "공고 상세", description = "채용 공고 상세 정보를 조회합니다.")
    @GetMapping("/{jobPostId}")
    public JobPostDto.JobPostResponse get(@PathVariable Long jobPostId) {
        return jobPostService.toResponse(jobPostService.get(jobPostId));
    }

    @Operation(summary = "주요 업무 개수", description = "공고의 주요 업무 항목 개수를 반환합니다. (구분자: 줄바꿈, 쉼표, 파이프)")
    @GetMapping("/{jobPostId}/main-tasks/count")
    public int mainTasksCount(@PathVariable Long jobPostId) {
        return jobPostService.countMainTasks(jobPostId);
    }
}
