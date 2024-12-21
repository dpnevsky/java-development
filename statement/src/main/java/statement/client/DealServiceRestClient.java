package statement.client;

import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import java.util.List;

public interface DealServiceRestClient {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto request);

    void selectLoanOffer(LoanOfferDto loanOfferDto);
}
