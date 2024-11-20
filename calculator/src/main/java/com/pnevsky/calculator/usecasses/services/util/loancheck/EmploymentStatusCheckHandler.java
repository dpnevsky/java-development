package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
public class EmploymentStatusCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        String status = scoringData.employment().employmentStatus().name();
        switch (status) {
            case "UNEMPLOYED" -> throw new LoanCheckException("Loan declined: Unemployed.");
            case "SELF_EMPLOYED" -> rate = rate.add(BigDecimal.valueOf(2));
            case "BUSINESS_OWNER" -> rate = rate.add(BigDecimal.valueOf(1));
        }
        return next(scoringData, rate);
    }
}

