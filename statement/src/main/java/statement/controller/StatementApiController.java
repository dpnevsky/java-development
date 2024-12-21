package statement.controller;

import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import statement.client.DealServiceRestClient;

import java.util.List;

@Tag(name = "Statement API Controller", description = "API for loan calculation")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/statement")
public class StatementApiController {

    private final DealServiceRestClient dealServiceRestClient;

    @Operation(summary = "Performs prescoring and issues loan offers")
    @PostMapping()
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        log.info("A request for loan offers has been sent to MC Deal: {}", loanStatementRequestDto);
        List<LoanOfferDto> offers = dealServiceRestClient.getLoanOffers(loanStatementRequestDto);
        log.info("Credit offers received from MC Deal: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "Select loan offer")
    @PostMapping("/offer")
    public void selectLoanOffer(@RequestBody LoanOfferDto loanOfferDto) {
        log.info("Selected offer: {}", loanOfferDto);
        dealServiceRestClient.selectLoanOffer(loanOfferDto);
        log.info("Selected offer sent to MC Deal: {}", loanOfferDto);
    }

}
