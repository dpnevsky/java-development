package statement.client.impl;

import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import statement.client.DealServiceRestClient;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DealServiceRestClientImpl implements DealServiceRestClient {

    private final RestClient restClient;

    @Value("${deal.statement-url}")
    private String dealStatementUrl;

    @Value("${deal.offer.select-url}")
    private String dealOfferSelectUrl;

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        ResponseEntity<List<LoanOfferDto>> response = restClient.post()
                .uri(dealStatementUrl)
                .body(loanStatementRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    @Override
    public void selectLoanOffer(LoanOfferDto loanOfferDto) {
        restClient.post()
                .uri(dealOfferSelectUrl)
                .body(loanOfferDto);
    }
}
