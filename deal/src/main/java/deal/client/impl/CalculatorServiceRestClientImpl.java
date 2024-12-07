package deal.client.impl;

import deal.client.CalculatorServiceRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import core.dto.CreditDto;
import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import core.dto.ScoringDataDto;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalculatorServiceRestClientImpl implements CalculatorServiceRestClient {

    private final RestClient restClient;

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        ResponseEntity<List<LoanOfferDto>> response = restClient.post()
                .uri("http://localhost:8080/calculator/offers")
                .body(loanStatementRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    @Override
    public CreditDto calculateLoan(ScoringDataDto scoringDataDto) {
        ResponseEntity<CreditDto> response = restClient.post()
                .uri("http://localhost:8080/calculator/calc")
                .body(scoringDataDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }
}
