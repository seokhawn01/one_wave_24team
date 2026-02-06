package com.gdg.workfit.domain;

import com.gdg.workfit.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "submission_scores")
public class SubmissionScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_result_id", nullable = false)
    private SubmissionResult submissionResult;

    @Column(nullable = false, length = 100)
    private String criteriaName;

    @Column(nullable = false)
    private Integer score;

    @Column(length = 1000)
    private String feedback;

    public SubmissionScore(SubmissionResult submissionResult, String criteriaName, Integer score, String feedback) {
        this.submissionResult = submissionResult;
        this.criteriaName = criteriaName;
        this.score = score;
        this.feedback = feedback;
    }
}
