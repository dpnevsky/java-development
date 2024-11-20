package com.pnevsky.calculator.usecasses.services;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;

public interface LoanCalculatorService {

    CreditDto calculateLoan(ScoringDataDto scoringDataDto) throws LoanCheckException;
}
