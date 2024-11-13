package com.pnevsky.calculator.usecasses.dto;

import com.pnevsky.calculator.persistence.model.GenderEnum;
import com.pnevsky.calculator.persistence.model.MaritalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(setterPrefix = "with")
public class ScoringDataDto {

    @Schema(description = "The requested amount for the loan", example = "25000.00")
    @DecimalMin(value = "20000.00", message = "Amount must be greater than or equal to 20000")
    private BigDecimal amount;

    @Schema(description = "The loan term in months", example = "12")
    @Min(value = 6, message = "Term must be greater than or equal to 6")
    private Integer term;

    @Schema(description = "The first name of the borrower", example = "John")
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must only contain Latin letters")
    private String firstName;

    @Schema(description = "The last name of the borrower", example = "Doe")
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must only contain Latin letters")
    private String lastName;

    @Schema(description = "The middle name of the borrower", example = "Michael")
    @Size(min = 2, max = 30, message = "Middle name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Middle name must only contain Latin letters")
    private String middleName;

    @Schema(description = "The gender of the borrower", example = "MALE")
    @NotNull(message = "Gender is required")
    private GenderEnum gender;

    @Schema(description = "The birthdate of the borrower in YYYY-MM-DD format", example = "1990-01-01")
    @Past(message = "Birthdate must be in the past and at least 18 years ago")
    private LocalDate birthdate;

    @Schema(description = "The series of the borrower's passport", example = "1234")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must be 4 digits")
    private String passportSeries;

    @Schema(description = "The number of the borrower's passport", example = "123456")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must be 6 digits")
    private String passportNumber;

    @Schema(description = "The issue date of the borrower's passport", example = "2015-06-20")
    private LocalDate passportIssueDate;

    @Schema(description = "The branch where the borrower's passport was issued", example = "New York City")
    @NotBlank(message = "Passport issue branch is required")
    private String passportIssueBranch;

    @Schema(description = "The marital status of the borrower", example = "SINGLE")
    @NotNull(message = "Marital status is required")
    private MaritalStatusEnum maritalStatus;

    @Schema(description = "The number of dependents of the borrower", example = "2")
    @Min(value = 0, message = "Dependent amount cannot be negative")
    private Integer dependentAmount;

    @Schema(description = "Employment details of the borrower")
    @NotNull(message = "Employment information is required")
    private EmploymentDto employment;

    @Schema(description = "The borrower's account number", example = "1234567890")
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @Schema(description = "Whether insurance is enabled for the loan", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Whether the borrower is a salary client", example = "true")
    private Boolean isSalaryClient;
}
