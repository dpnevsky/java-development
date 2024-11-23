package calculator.service.util.loancheck;

import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;
import org.springframework.stereotype.Component;
import calculator.service.util.Constant;

import java.math.BigDecimal;

@Component
public class WorkExperienceCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        int totalExperience = scoringData.employment().workExperienceTotal();
        int currentExperience = scoringData.employment().workExperienceCurrent();
        if (totalExperience < Constant.MIN_TOTAL_EXPERIENCE_FOR_CREDIT_IN_MONTH) {
            throw new LoanCheckException("Loan declined: Total work experience is less than 18 months.");
        }
        if (currentExperience < Constant.MIN_CURRENT_EXPERIENCE_FOR_CREDIT_IN_MONTH) {
            throw new LoanCheckException("Loan declined: Current work experience is less than 3 months.");
        }
        return rate;
    }
}
