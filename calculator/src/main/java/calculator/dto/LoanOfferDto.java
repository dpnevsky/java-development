package calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder(setterPrefix = "with")
public record LoanOfferDto(
        @Schema(description = "The unique identifier for the loan statement", example = "e4c0ab2d-3b85-4bfb-a62b-0a1cb6b61816")
        @NotNull(message = "Statement ID cannot be null")
        UUID statementId,

        @Schema(description = "The amount requested by the borrower", example = "25000.00")
        @NotNull(message = "Requested amount cannot be null")
        @DecimalMin(value = "0.01", message = "Requested amount must be greater than 0")
        BigDecimal requestedAmount,

        @Schema(description = "The total amount of the loan offered", example = "30000.00")
        @NotNull(message = "Total amount cannot be null")
        @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
        BigDecimal totalAmount,

        @Schema(description = "The term of the loan in months", example = "12")
        @NotNull(message = "Loan term cannot be null")
        @Min(value = 1, message = "Loan term must be at least 1 month")
        Integer term,

        @Schema(description = "The monthly payment amount for the loan", example = "2500.00")
        @NotNull(message = "Monthly payment cannot be null")
        @DecimalMin(value = "0.01", message = "Monthly payment must be greater than 0")
        BigDecimal monthlyPayment,

        @Schema(description = "The annual interest rate for the loan", example = "5.5")
        @NotNull(message = "Interest rate cannot be null")
        @DecimalMin(value = "0.01", message = "Interest rate must be greater than 0")
        BigDecimal rate,

        @Schema(description = "Indicates whether insurance is enabled for the loan", example = "true")
        @NotNull(message = "Insurance status cannot be null")
        Boolean isInsuranceEnabled,

        @Schema(description = "Indicates whether the borrower is a salary client", example = "true")
        @NotNull(message = "Salary client status cannot be null")
        Boolean isSalaryClient
) implements Serializable {}
