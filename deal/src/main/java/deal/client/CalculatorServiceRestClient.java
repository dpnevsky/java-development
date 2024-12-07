package deal.client;

import core.dto.CreditDto;
import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import core.dto.ScoringDataDto;
import java.util.List;

public interface CalculatorServiceRestClient {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto request);

    CreditDto calculateLoan(ScoringDataDto scoringDataDto);
}
