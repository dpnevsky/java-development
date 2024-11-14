package com.pnevsky.calculator.api.controllers;

import com.pnevsky.calculator.usecasses.LoanCalculatorService;
import com.pnevsky.calculator.usecasses.LoanOfferService;
import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.LoanOfferDto;
import com.pnevsky.calculator.usecasses.dto.LoanStatementRequestDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "Save additional information in the user's CV")
    @PostMapping("/offers")
    public List<LoanOfferDto> generateLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received request for generating loan offers: {}", loanStatementRequestDto);
        List<LoanOfferDto> offers = loanOfferService.generateLoanOffers(loanStatementRequestDto);
        log.info("Generated loan offers: {}", offers);
        return offers;
    }

    @Operation(summary = "Save additional information in the user's CV")
    @PostMapping("/calc")
    public CreditDto calculateLoan(@RequestBody ScoringDataDto scoringDataDto) {
        log.info("Received scoring data for loan calculation: {}", scoringDataDto);
        CreditDto creditDto = loanCalculatorService.calculateLoan(scoringDataDto);
        log.info("Calculated loan details: {}", creditDto);
        return creditDto;
    }
}
