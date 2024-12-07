package core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import core.type.GenderType;
import core.type.MaritalStatusType;
import java.io.Serializable;
import java.time.LocalDate;

@Builder(setterPrefix = "with")
public record FinishRegistrationRequestDto(

        @Schema(description = "Gender of the user", example = "MALE")
        @NotNull(message = "Gender is required")
        GenderType gender,

        @Schema(description = "Marital status of the user", example = "MARRIED", allowableValues = "MARRIED, SINGLE, DIVORCED")
        @NotNull(message = "Marital status is required")
        MaritalStatusType maritalStatus,

        @Schema(description = "Number of dependents", example = "2")
        @NotNull(message = "Dependent amount is required")
        @Min(value = 0, message = "Dependent amount cannot be negative")
        Integer dependentAmount,

        @Schema(description = "Passport issue date", example = "2020-01-01")
        @NotNull(message = "Passport issue date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate passportIssueDate,

        @Schema(description = "Passport issue branch", example = "Moscow")
        @NotNull(message = "Passport issue branch is required")
        String passportIssueBranch,

        @Schema(description = "Employment details of the borrower")
        @NotNull(message = "Employment information is required")
        @Valid
        EmploymentDto employment,

        @Schema(description = "Account number", example = "123456789")
        @NotNull(message = "Account number is required")
        String accountNumber
) implements Serializable {}

