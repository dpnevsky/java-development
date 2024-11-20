package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import java.math.BigDecimal;

public class WorkExperienceCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        int totalExperience = scoringData.employment().workExperienceTotal();
        int currentExperience = scoringData.employment().workExperienceCurrent();
        if (totalExperience < 18)
            throw new LoanCheckException("Loan declined: Total work experience is less than 18 months.");
        if (currentExperience < 3)
            throw new LoanCheckException("Loan declined: Current work experience is less than 3 months.");
        return next(scoringData, rate);
    }
}

