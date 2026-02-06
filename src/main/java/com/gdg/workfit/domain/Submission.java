package com.gdg.workfit.domain;

import com.gdg.workfit.common.BaseEntity;
import jakarta.persistence.Column;
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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "submissions")
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @Column(nullable = false, length = 100)
    private String applicantName;

    @Column(nullable = false, length = 200)
    private String applicantEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubmissionStatus status;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime submittedAt;

    @Column(length = 6000)
    private String draftAnswer;

    @Column(length = 6000)
    private String finalAnswer;

    public Submission(JobPost jobPost, String applicantName, String applicantEmail) {
        this.jobPost = jobPost;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.status = SubmissionStatus.STARTED;
        this.startedAt = LocalDateTime.now();
    }

    public void saveDraft(String draftAnswer) {
        this.draftAnswer = draftAnswer;
        this.status = SubmissionStatus.DRAFT;
    }

    public void submit(String finalAnswer) {
        this.finalAnswer = finalAnswer;
        this.submittedAt = LocalDateTime.now();
        this.status = SubmissionStatus.SUBMITTED;
    }

    public void markEvaluated() {
        this.status = SubmissionStatus.EVALUATED;
    }
}
