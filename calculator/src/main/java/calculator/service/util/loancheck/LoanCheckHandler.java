package calculator.service.util.loancheck;

import calculator.exception.LoanCheckException;
import core.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface LoanCheckHandler {
    BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException;
}
