package com.pnevsky.calculator.usecasses.dto;

import com.pnevsky.calculator.usecasses.types.EmploymentStatusType;
import com.pnevsky.calculator.usecasses.types.PositionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder(setterPrefix = "with")
public record EmploymentDto (
    @Schema(description = "Employment status of the borrower", example = "EMPLOYED")
    @NotNull(message = "Employment status is required")
    EmploymentStatusType employmentStatus,

    @Schema(description = "Employer's INN", example = "1234567890")
    @Pattern(regexp = "^\\d{10}$", message = "Employer INN must be a 10-digit number")
    String employerINN,

    @Schema(description = "Salary of the borrower", example = "3500.00")
    @DecimalMin(value = "0.01", message = "Salary must be greater than zero")
    BigDecimal salary,

    @Schema(description = "Position held by the borrower", example = "MANAGER")
    @NotNull(message = "Position is required")
    PositionType position,

    @Schema(description = "Total work experience of the borrower in month", example = "19")
    @Min(value = 0, message = "Total work experience must be greater than or equal to 0")
    Integer workExperienceTotal,

    @Schema(description = "Current work experience of the borrower in month", example = "5")
    @Min(value = 0, message = "Current work experience must be greater than or equal to 0")
    Integer workExperienceCurrent
) implements Serializable {}
