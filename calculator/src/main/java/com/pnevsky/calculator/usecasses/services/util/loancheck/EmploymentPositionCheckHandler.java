package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;

import java.math.BigDecimal;

public class EmploymentPositionCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        String position = scoringData.employment().position().name();
        if (position.equalsIgnoreCase("MID_MANAGER")) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else if (position.equalsIgnoreCase("TOP_MANAGER")) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }
        return next(scoringData, rate);
    }
}
