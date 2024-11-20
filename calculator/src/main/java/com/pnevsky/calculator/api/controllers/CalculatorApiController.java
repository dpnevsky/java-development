package com.pnevsky.calculator.api.controllers;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.services.LoanCalculatorService;
import com.pnevsky.calculator.usecasses.services.LoanOfferService;
import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.LoanOfferDto;
import com.pnevsky.calculator.usecasses.dto.LoanStatementRequestDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<LoanOfferDto>> generateLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received request for generating loan offers: {}", loanStatementRequestDto);
        List<LoanOfferDto> offers = loanOfferService.generateLoanOffers(loanStatementRequestDto);
        log.info("Generated loan offers: {}", offers);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @Operation(summary = "Scores data and calculates the final rate, the total cost of the loan (psk), " +
            "the amount of the monthly payment, and the monthly payment schedule")
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateLoan(@RequestBody @Valid ScoringDataDto scoringDataDto) throws LoanCheckException {
        log.info("Received scoring data for loan calculation: {}", scoringDataDto);
        CreditDto creditDto = loanCalculatorService.calculateLoan(scoringDataDto);
        log.info("Calculated loan details: {}", creditDto);
        return new ResponseEntity<>(creditDto, HttpStatus.OK);
    }
}
