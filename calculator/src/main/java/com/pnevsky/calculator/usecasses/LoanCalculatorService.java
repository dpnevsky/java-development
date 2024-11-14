package com.pnevsky.calculator.usecasses;

import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;

public interface LoanCalculatorService {

    CreditDto calculateLoan(ScoringDataDto scoringDataDto);
}
