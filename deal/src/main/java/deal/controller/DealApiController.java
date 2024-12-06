package deal.controller;

import deal.client.CalculatorServiceRestClient;
import deal.mapper.CreditMapper;
import deal.persistence.model.Client;
import deal.persistence.model.Credit;
import deal.persistence.model.Statement;
import deal.service.ClientService;
import deal.service.CreditService;
import deal.service.LoanStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.dto.CreditDto;
import core.dto.FinishRegistrationRequestDto;
import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import core.dto.ScoringDataDto;
import core.type.ApplicationStatusType;
import java.util.List;
import java.util.UUID;

@Tag(name = "Deal API Controller", description = "API for preparing the deal")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealApiController {

    private final LoanStatementService loanStatementService;
    private final CalculatorServiceRestClient calculatorServiceRestClient;
    private final ClientService clientService;
    private final CreditService creditService;
    private final CreditMapper creditMapper;

    @Operation(summary = "Create client and statement, send request for performs prescoring at Calculator API and issues loan offers")
    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received request to create client and statement for loan with amount: {}", loanStatementRequestDto.amount());

        Client client = clientService.saveClient(loanStatementRequestDto);
        log.debug("Client created with ID: {}", client.getClientId());

        Statement statement = loanStatementService.saveStatement(client);
        log.debug("Statement created with ID: {}", statement.getStatementId());

        List<LoanOfferDto> offers = calculatorServiceRestClient.getLoanOffers(loanStatementRequestDto);
        offers.forEach(offer -> offer.setStatementId(statement.getStatementId()));
        log.info("Loan offers retrieved: {}", offers.size());

        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "Select loan offer and update statement status")
    @PostMapping("/offer/select")
    public void selectLoanOffer(@RequestBody @Valid LoanOfferDto loanOfferDto) {
        log.info("Received request to select loan offer for statement ID: {}", loanOfferDto.getStatementId());

        Statement statement = loanStatementService.selectLoanOfferByStatementId(loanOfferDto.getStatementId());
        if (statement == null) {
            log.error("Statement with ID {} not found", loanOfferDto.getStatementId());
        }

        loanStatementService.updateStatement(statement, loanOfferDto, ApplicationStatusType.PREAPPROVAL);
        loanStatementService.saveStatement(statement);
        log.info("Loan offer selected and statement updated with status PREAPPROVAL for statement ID: {}", loanOfferDto.getStatementId());
    }

    @Operation(summary = "Calculate the loan and update the statement status")
    @PostMapping("/calculate/{statementId}")
    public void calculateLoan(@PathVariable(name = "statementId") String statementId,
                                                   @RequestBody @Valid FinishRegistrationRequestDto finishRegistrationRequestDto) {
        log.info("Received request to calculate loan for statement ID: {}", statementId);

        Statement statement = loanStatementService.getStatementById(UUID.fromString(statementId));
        if (statement == null) {
            log.error("Statement with ID {} not found", statementId);
            return;
        }

        Client client = statement.getClientID();
        LoanOfferDto appliedOffer = statement.getAppliedOffer();

        log.info("Preparing scoring data for statement ID: {}", statementId);

        ScoringDataDto scoringDataDto = loanStatementService.buildScoringData(client, appliedOffer, finishRegistrationRequestDto);

        log.info("Sending scoring data to calculator service for statement ID: {}", statementId);
        CreditDto creditDto = calculatorServiceRestClient.calculateLoan(scoringDataDto);

        log.info("Received credit data from calculator: {}", creditDto);

        Credit credit = creditMapper.fromDtoToEntity(creditDto);

        statement.setCreditID(credit);
        creditService.saveCredit(credit);
        log.debug("Credit data saved for statement ID: {}", statementId);

        loanStatementService.updateStatement(statement, ApplicationStatusType.APPROVED);
        loanStatementService.saveStatement(statement);
        log.info("Statement updated with status APPROVED for statement ID: {}", statementId);
    }
}
