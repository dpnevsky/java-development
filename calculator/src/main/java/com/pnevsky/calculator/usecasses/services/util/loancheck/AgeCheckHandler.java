package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AgeCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        int age = LocalDate.now().getYear() - scoringData.birthdate().getYear();
        if (age < 20 || age > 65) {
            throw new LoanCheckException("Loan declined: Age not within acceptable range.");
        }
        return next(scoringData, rate);
    }
}
