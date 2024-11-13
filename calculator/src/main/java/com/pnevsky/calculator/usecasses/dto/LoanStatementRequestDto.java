package com.pnevsky.calculator.usecasses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO for Loan Statement Request")
@Builder(setterPrefix = "with")
public class LoanStatementRequestDto {

    @Schema(description = "The amount of the loan request", defaultValue = "20000.00")
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "20000.00", message = "Amount must be greater than or equal to 20000")
    private BigDecimal amount;

    @Schema(description = "The term of the loan in months", defaultValue = "12")
    @NotNull(message = "Term cannot be null")
    @Min(value = 6, message = "Term must be at least 6 months")
    private Integer term;

    @Schema(description = "The first name of the borrower", defaultValue = "John")
    @NotNull(message = "First name cannot be null")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "First name must be between 2 and 30 Latin letters")
    private String firstName;

    @Schema(description = "The last name of the borrower", defaultValue = "Doe")
    @NotNull(message = "Last name cannot be null")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Last name must be between 2 and 30 Latin letters")
    private String lastName;

    @Schema(description = "The middle name of the borrower (if any)", defaultValue = "Edward")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Middle name must be between 2 and 30 Latin letters")
    private String middleName;

    @Schema(description = "The email address of the borrower", defaultValue = "john.doe@defaultValue.com")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be a valid address")
    private String email;

    @Schema(description = "The birthdate of the borrower", defaultValue = "2000-01-01")
    @NotNull(message = "Birthdate cannot be null")
    @Past(message = "Birthdate must be in the past and at least 18 years ago")
    private LocalDate birthdate;

    @Schema(description = "The passport series of the borrower", defaultValue = "1234")
    @NotNull(message = "Passport series cannot be null")
    @Pattern(regexp = "^[0-9]{4}$", message = "Passport series must be 4 digits")
    private String passportSeries;

    @Schema(description = "The passport number of the borrower", defaultValue = "123456")
    @NotNull(message = "Passport number cannot be null")
    @Pattern(regexp = "^[0-9]{6}$", message = "Passport number must be 6 digits")
    private String passportNumber;
}
