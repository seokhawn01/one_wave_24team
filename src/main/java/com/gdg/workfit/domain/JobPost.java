package com.gdg.workfit.domain;

import com.gdg.workfit.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "job_posts")
public class JobPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id", nullable = false)
    private Prompt prompt;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "job_post_main_tasks", joinColumns = @JoinColumn(name = "job_post_id"))
    @Column(name = "task", nullable = false, length = 500)
    private List<String> mainTasks = new ArrayList<>();

    @Column(nullable = false, length = 2000)
    private String qualifications;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JobCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DifficultyLevel difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobPostStatus status;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private LocalDate closeDate;

    @Column
    private Integer retentionDays;

    @Column
    private Integer testDurationMinutes;

    @Column(length = 2000)
    private String testInfo;

    public JobPost(
            Company company,
            Prompt prompt,
            String title,
            String description,
            List<String> mainTasks,
            String qualifications,
            JobCategory category,
            DifficultyLevel difficulty,
            LocalDate openDate,
            LocalDate closeDate,
            Integer retentionDays,
            Integer testDurationMinutes,
            String testInfo
    ) {
        this.company = company;
        this.prompt = prompt;
        this.title = title;
        this.description = description;
        this.mainTasks = mainTasks != null ? new ArrayList<>(mainTasks) : new ArrayList<>();
        this.qualifications = qualifications;
        this.category = category;
        this.difficulty = difficulty;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.retentionDays = retentionDays;
        this.testDurationMinutes = testDurationMinutes;
        this.testInfo = testInfo;
        this.status = JobPostStatus.OPEN;
    }

    public void update(
            String title,
            String description,
            List<String> mainTasks,
            String qualifications,
            JobCategory category,
            DifficultyLevel difficulty,
            LocalDate openDate,
            LocalDate closeDate,
            Integer retentionDays,
            Integer testDurationMinutes,
            String testInfo
    ) {
        this.title = title;
        this.description = description;
        this.mainTasks = mainTasks != null ? new ArrayList<>(mainTasks) : new ArrayList<>();
        this.qualifications = qualifications;
        this.category = category;
        this.difficulty = difficulty;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.retentionDays = retentionDays;
        this.testDurationMinutes = testDurationMinutes;
        this.testInfo = testInfo;
    }

    public void close() {
        this.status = JobPostStatus.CLOSED;
    }
}
