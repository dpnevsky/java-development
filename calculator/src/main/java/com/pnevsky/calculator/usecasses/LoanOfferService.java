package com.pnevsky.calculator.usecasses;

import com.pnevsky.calculator.usecasses.dto.LoanOfferDto;
import com.pnevsky.calculator.usecasses.dto.LoanStatementRequestDto;
import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto loanStatementRequestDto);
}
