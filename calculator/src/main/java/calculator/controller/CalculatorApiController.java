package calculator.controller;

import calculator.exception.LoanCheckException;
import calculator.service.LoanCalculatorService;
import calculator.service.LoanOfferService;
import core.dto.CreditDto;
import core.dto.LoanOfferDto;
import core.dto.LoanStatementRequestDto;
import core.dto.ScoringDataDto;
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

import java.util.List;

@Tag(name = "Calculator API Controller", description = "API for loan calculation")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/calculator")
public class CalculatorApiController {

    private final LoanOfferService loanOfferService;
    private final LoanCalculatorService loanCalculatorService;

    @Operation(summary = "Performs prescoring and issues loan offers")
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> generateLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received request for generating loan offers: {}", loanStatementRequestDto);
        List<LoanOfferDto> offers = loanOfferService.generateLoanOffers(loanStatementRequestDto);
        log.info("Generated loan offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "Scores data and calculates the final rate, the total cost of the loan (psk), " +
            "the amount of the monthly payment, and the monthly payment schedule")
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateLoan(@RequestBody @Valid ScoringDataDto scoringDataDto) throws LoanCheckException {
        log.info("Received scoring data for loan calculation: {}", scoringDataDto);
        CreditDto creditDto = loanCalculatorService.calculateLoan(scoringDataDto);
        log.info("Calculated loan details: {}", creditDto);
        return ResponseEntity.ok(creditDto);
    }
}
