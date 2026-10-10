package calculator.service;

import calculator.exception.LoanCheckException;
import core.dto.CreditDto;
import core.dto.ScoringDataDto;

public interface LoanCalculatorService {

    CreditDto calculateLoan(ScoringDataDto scoringDataDto) throws LoanCheckException;
}
