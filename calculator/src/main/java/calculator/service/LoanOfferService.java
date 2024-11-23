package calculator.service;

import calculator.dto.LoanOfferDto;
import calculator.dto.LoanStatementRequestDto;
import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto loanStatementRequestDto);
}
