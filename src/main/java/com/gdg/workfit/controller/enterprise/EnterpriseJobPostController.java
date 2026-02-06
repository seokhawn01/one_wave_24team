package com.gdg.workfit.controller.enterprise;

import com.gdg.workfit.dto.enterprise.JobPostDto;
import com.gdg.workfit.service.JobPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "기업-공고", description = "기업이 채용 공고를 생성/조회/수정/종료합니다.")
@RestController
@RequestMapping("/api/enterprise/job-posts")
public class EnterpriseJobPostController {

    private final JobPostService jobPostService;

    public EnterpriseJobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @Operation(summary = "공고 생성", description = "채용 공고를 생성합니다.")
    @PostMapping
    public JobPostDto.JobPostResponse create(@RequestBody JobPostDto.CreateJobPostRequest request) {
        return jobPostService.toResponse(jobPostService.create(request));
    }

    @Operation(summary = "공고 목록", description = "기업의 채용 공고 목록을 조회합니다. companyId를 주면 해당 기업만 조회됩니다.")
    @GetMapping
    public List<JobPostDto.JobPostResponse> list(@RequestParam(required = false) Long companyId) {
        return jobPostService.findByCompanyId(companyId).stream()
                .map(jobPostService::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "공고 상세", description = "특정 채용 공고의 상세 정보를 조회합니다.")
    @GetMapping("/{jobPostId}")
    public JobPostDto.JobPostResponse get(@PathVariable Long jobPostId) {
        return jobPostService.toResponse(jobPostService.get(jobPostId));
    }

    @Operation(summary = "공고 수정", description = "채용 공고 정보를 수정합니다.")
    @PutMapping("/{jobPostId}")
    public JobPostDto.JobPostResponse update(
            @PathVariable Long jobPostId,
            @RequestBody JobPostDto.UpdateJobPostRequest request
    ) {
        return jobPostService.toResponse(jobPostService.update(jobPostId, request));
    }

    @Operation(summary = "공고 종료", description = "채용 공고를 마감 처리합니다.")
    @DeleteMapping("/{jobPostId}")
    public void close(@PathVariable Long jobPostId) {
        jobPostService.close(jobPostId);
    }
}
