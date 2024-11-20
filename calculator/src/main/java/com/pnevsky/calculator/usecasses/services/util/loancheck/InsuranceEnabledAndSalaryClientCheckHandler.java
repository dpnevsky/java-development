package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;

import java.math.BigDecimal;

public class InsuranceEnabledAndSalaryClientCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        if (scoringData.isInsuranceEnabled())
            rate = rate.subtract(BigDecimal.ONE);
        if (scoringData.isSalaryClient())
            rate = rate.subtract(BigDecimal.valueOf(3));
        return next(scoringData, rate);
    }
}