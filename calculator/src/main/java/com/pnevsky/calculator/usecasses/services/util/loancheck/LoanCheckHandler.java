package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import java.math.BigDecimal;

public interface LoanCheckHandler {
    BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException;
    void setNext(LoanCheckHandler handler);
}
