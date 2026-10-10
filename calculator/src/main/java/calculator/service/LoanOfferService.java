package calculator.service;

import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto loanStatementRequestDto);
}
