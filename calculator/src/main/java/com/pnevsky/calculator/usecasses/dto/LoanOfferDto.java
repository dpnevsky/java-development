package com.pnevsky.calculator.usecasses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder(setterPrefix = "with")
public record LoanOfferDto (
    @Schema(description = "The unique identifier for the loan statement", example = "e4c0ab2d-3b85-4bfb-a62b-0a1cb6b61816")
    UUID statementId,
    @Schema(description = "The amount requested by the borrower", example = "25000.00")
    BigDecimal requestedAmount,
    @Schema(description = "The total amount of the loan offered", example = "30000.00")
    BigDecimal totalAmount,
    @Schema(description = "The term of the loan in months", example = "12")
    Integer term,
    @Schema(description = "The monthly payment amount for the loan", example = "2500.00")
    BigDecimal monthlyPayment,
    @Schema(description = "The annual interest rate for the loan", example = "5.5")
    BigDecimal rate,
    @Schema(description = "Indicates whether insurance is enabled for the loan", example = "true")
    Boolean isInsuranceEnabled,
    @Schema(description = "Indicates whether the borrower is a salary client", example = "true")
    Boolean isSalaryClient
) implements Serializable {
}
