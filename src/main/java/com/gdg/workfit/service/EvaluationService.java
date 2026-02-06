package com.gdg.workfit.service;

import com.gdg.workfit.domain.Submission;
import com.gdg.workfit.domain.SubmissionResult;
import com.gdg.workfit.domain.SubmissionScore;
import com.gdg.workfit.dto.system.EvaluationDto;
import com.gdg.workfit.repository.SubmissionResultRepository;
import com.gdg.workfit.repository.SubmissionScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EvaluationService {

    private final SubmissionResultRepository submissionResultRepository;
    private final SubmissionScoreRepository submissionScoreRepository;

    public EvaluationService(
            SubmissionResultRepository submissionResultRepository,
            SubmissionScoreRepository submissionScoreRepository
    ) {
        this.submissionResultRepository = submissionResultRepository;
        this.submissionScoreRepository = submissionScoreRepository;
    }

    public SubmissionResult evaluate(Submission submission) {
        String answer = submission.getFinalAnswer() != null ? submission.getFinalAnswer() : submission.getDraftAnswer();
        EvaluationDto.Evaluation evaluation = buildEvaluation(answer);

        int total = average(
                evaluation.problemUnderstanding().score(),
                evaluation.solutionLogic().score(),
                evaluation.technicalFeasibility().score(),
                evaluation.practicalApplicability().score(),
                evaluation.communication().score()
        );

        boolean passed = isPass(total, evaluation.practicalApplicability().score(), evaluation.solutionLogic().score());

        SubmissionResult result = submissionResultRepository.save(
                new SubmissionResult(submission, passed, total, "기업 평가원 기준의 요약 피드백입니다.")
        );

        submissionScoreRepository.save(new SubmissionScore(result, "문제 분석 정확도", evaluation.problemUnderstanding().score(), evaluation.problemUnderstanding().comment()));
        submissionScoreRepository.save(new SubmissionScore(result, "해결 전략의 논리성", evaluation.solutionLogic().score(), evaluation.solutionLogic().comment()));
        submissionScoreRepository.save(new SubmissionScore(result, "기술적/업무적 타당성", evaluation.technicalFeasibility().score(), evaluation.technicalFeasibility().comment()));
        submissionScoreRepository.save(new SubmissionScore(result, "현실 적용 가능성", evaluation.practicalApplicability().score(), evaluation.practicalApplicability().comment()));
        submissionScoreRepository.save(new SubmissionScore(result, "커뮤니케이션 명확성", evaluation.communication().score(), evaluation.communication().comment()));

        submission.markEvaluated();
        return result;
    }

    public EvaluationDto.Evaluation buildEvaluation(String answer) {
        int length = answer == null ? 0 : answer.trim().length();
        int base = scoreByLength(length);

        return new EvaluationDto.Evaluation(
                new EvaluationDto.Item(base + 5, "문제 핵심을 어느 정도 정확히 파악했습니다."),
                new EvaluationDto.Item(base, "해결 접근이 논리적으로 이어집니다."),
                new EvaluationDto.Item(base - 5, "기술/업무 타당성은 추가 근거가 필요합니다."),
                new EvaluationDto.Item(base - 5, "현실 적용 시 리스크 설명이 보완되면 좋습니다."),
                new EvaluationDto.Item(base + 5, "전달 구조가 비교적 명확합니다.")
        );
    }

    private int scoreByLength(int length) {
        if (length >= 400) {
            return 85;
        }
        if (length >= 200) {
            return 75;
        }
        if (length >= 80) {
            return 65;
        }
        if (length > 0) {
            return 55;
        }
        return 40;
    }

    private int average(int... values) {
        int total = 0;
        for (int value : values) {
            total += value;
        }
        return Math.round(total / (float) values.length);
    }

    private boolean isPass(int totalScore, int practicalScore, int logicScore) {
        if (practicalScore < 50) {
            return false;
        }
        if (totalScore >= 70) {
            return true;
        }
        return totalScore >= 65 && logicScore >= 70;
    }
}
