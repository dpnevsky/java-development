package com.pnevsky.calculator.usecasses.dto;

import com.pnevsky.calculator.usecasses.types.GenderType;
import com.pnevsky.calculator.usecasses.types.MaritalStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(setterPrefix = "with")
public record ScoringDataDto (
    @Schema(description = "The requested amount for the loan", example = "20000.00")
    @DecimalMin(value = "20000.00", message = "Amount must be greater than or equal to 20000")
    BigDecimal amount,

    @Schema(description = "The loan term in months", example = "12")
    @Min(value = 6, message = "Term must be greater than or equal to 6")
    Integer term,

    @Schema(description = "The first name of the borrower", example = "Ivan")
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must only contain Latin letters")
    String firstName,

    @Schema(description = "The last name of the borrower", example = "Ivanov")
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must only contain Latin letters")
    String lastName,

    @Schema(description = "The middle name of the borrower", example = "Dmitrievich")
    @Size(min = 2, max = 30, message = "Middle name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Middle name must only contain Latin letters")
    String middleName,

    @Schema(description = "The gender of the borrower", example = "MALE")
    @NotNull(message = "Gender is required")
    GenderType gender,

    @Schema(description = "The birthdate of the borrower in YYYY-MM-DD format", example = "1990-01-01")
    @Past(message = "Birthdate must be in the past and at least 18 years ago")
    LocalDate birthdate,

    @Schema(description = "The series of the borrower's passport", example = "1234")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must be 4 digits")
    String passportSeries,

    @Schema(description = "The number of the borrower's passport", example = "123456")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must be 6 digits")
    String passportNumber,

    @Schema(description = "The issue date of the borrower's passport", example = "2015-06-20")
    @Past(message = "Passport issue must be in the past")
    LocalDate passportIssueDate,

    @Schema(description = "The branch where the borrower's passport was issued", example = "New York City")
    @NotBlank(message = "Passport issue branch is required")
    String passportIssueBranch,

    @Schema(description = "The marital status of the borrower", example = "SINGLE")
    @NotNull(message = "Marital status is required")
    MaritalStatusType maritalStatus,

    @Schema(description = "The number of dependents of the borrower", example = "2")
    @Min(value = 0, message = "Dependent amount cannot be negative")
    Integer dependentAmount,

    @Schema(description = "Employment details of the borrower")
    @NotNull(message = "Employment information is required")
    @Valid
    EmploymentDto employment,

    @Schema(description = "The borrower's account number", example = "1234567890")
    @NotBlank(message = "Account number is required")
    String accountNumber,

    @Schema(description = "Whether insurance is enabled for the loan", example = "true")
    Boolean isInsuranceEnabled,

    @Schema(description = "Whether the borrower is a salary client", example = "true")
    Boolean isSalaryClient
) implements Serializable {}
