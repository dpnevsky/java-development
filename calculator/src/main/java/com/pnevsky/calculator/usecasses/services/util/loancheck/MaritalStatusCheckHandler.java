package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import java.math.BigDecimal;

public class MaritalStatusCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        switch (scoringData.maritalStatus().name()) {
            case "MARRIED" -> rate = rate.subtract(BigDecimal.valueOf(3));
            case "DIVORCED" -> rate = rate.add(BigDecimal.valueOf(1));
        }
        return next(scoringData, rate);
    }
}
