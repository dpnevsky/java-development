package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;

import java.math.BigDecimal;

public class SalaryAndAmountCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        BigDecimal salary = scoringData.employment().salary();
        BigDecimal amount = scoringData.amount();

        if (amount.compareTo(salary.multiply(BigDecimal.valueOf(24))) > 0) {
            throw new LoanCheckException("Loan declined: Requested amount exceeds 24 months of salary.");
        }
        return next(scoringData, rate);
    }
}

