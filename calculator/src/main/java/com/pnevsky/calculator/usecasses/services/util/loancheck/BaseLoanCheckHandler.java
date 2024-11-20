package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
public abstract class BaseLoanCheckHandler implements LoanCheckHandler {
    private LoanCheckHandler next;

    protected BigDecimal next(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        if (next != null) {
            return next.handle(scoringData, rate);
        }
        return rate;
    }
}
