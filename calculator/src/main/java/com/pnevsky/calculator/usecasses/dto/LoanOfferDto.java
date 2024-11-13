package com.pnevsky.calculator.usecasses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class LoanOfferDto {

    @Schema(description = "The unique identifier for the loan statement", example = "e4c0ab2d-3b85-4bfb-a62b-0a1cb6b61816")
    private UUID statementId;

    @Schema(description = "The amount requested by the borrower", example = "25000.00")
    private BigDecimal requestedAmount;

    @Schema(description = "The total amount of the loan offered", example = "30000.00")
    private BigDecimal totalAmount;

    @Schema(description = "The term of the loan in months", example = "12")
    private Integer term;

    @Schema(description = "The monthly payment amount for the loan", example = "2500.00")
    private BigDecimal monthlyPayment;

    @Schema(description = "The annual interest rate for the loan", example = "5.5")
    private BigDecimal rate;

    @Schema(description = "Indicates whether insurance is enabled for the loan", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Indicates whether the borrower is a salary client", example = "true")
    private Boolean isSalaryClient;
}
