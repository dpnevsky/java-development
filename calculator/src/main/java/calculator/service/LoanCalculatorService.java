package calculator.service;

import calculator.dto.CreditDto;
import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;

public interface LoanCalculatorService {

    CreditDto calculateLoan(ScoringDataDto scoringDataDto) throws LoanCheckException;
}
