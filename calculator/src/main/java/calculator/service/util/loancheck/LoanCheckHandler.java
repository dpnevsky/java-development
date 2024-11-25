package calculator.service.util.loancheck;

import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;

import java.math.BigDecimal;

public interface LoanCheckHandler {
    BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException;
}
