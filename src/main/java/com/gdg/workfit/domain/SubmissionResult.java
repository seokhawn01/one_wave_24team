package com.gdg.workfit.domain;

import com.gdg.workfit.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "submission_results")
public class SubmissionResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @Column(nullable = false)
    private Boolean passed;

    @Column(nullable = false)
    private Integer totalScore;

    @Column(length = 2000)
    private String feedbackSummary;

    public SubmissionResult(Submission submission, Boolean passed, Integer totalScore, String feedbackSummary) {
        this.submission = submission;
        this.passed = passed;
        this.totalScore = totalScore;
        this.feedbackSummary = feedbackSummary;
    }
}
