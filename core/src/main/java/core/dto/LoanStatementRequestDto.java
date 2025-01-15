package core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Schema(description = "DTO for Loan Statement Request")
@Builder(setterPrefix = "with")
public record LoanStatementRequestDto (
        @Schema(description = "The amount of the loan request", example = "20000.00", defaultValue = "20000.00")
        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "20000.00", message = "Amount must be greater than or equal to 20000")
        BigDecimal amount,

        @Schema(description = "The term of the loan in months", example = "12", defaultValue = "12")
        @NotNull(message = "Term cannot be null")
        @Min(value = 6, message = "Term must be at least 6 months")
        Integer term,

        @Schema(description = "The first name of the borrower", example = "Ivan", defaultValue = "Ivan")
        @NotNull(message = "First name cannot be null")
        @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "First name must be between 2 and 30 Latin letters")
        String firstName,

        @Schema(description = "The last name of the borrower", example = "Ivanov", defaultValue = "Ivanov")
        @NotNull(message = "Last name cannot be null")
        @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Last name must be between 2 and 30 Latin letters")
        String lastName,

        @Schema(description = "The middle name of the borrower (if any)", example = "Dmitrievich", defaultValue = "Dmitrievich")
        @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Middle name must be between 2 and 30 Latin letters")
        String middleName,

        @Schema(description = "The email address of the borrower", example = "ivanovforexample@yandex.ru", defaultValue = "ivanovforexample@yandex.ru")
        @NotNull(message = "Email cannot be null")
        @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$", message = "Email must be a valid address")
        String email,

        @Schema(description = "The birthdate of the borrower", example = "2000-01-01", defaultValue = "2000-01-01")
        @NotNull(message = "Birthdate cannot be null")
        @Past(message = "Birthdate must be in the past and at least 18 years ago")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthdate,

        @Schema(description = "The passport series of the borrower", example = "1234", defaultValue = "1234")
        @NotNull(message = "Passport series cannot be null")
        @Pattern(regexp = "^[0-9]{4}$", message = "Passport series must be 4 digits")
        String passportSeries,

        @Schema(description = "The passport number of the borrower", example = "654321", defaultValue = "654321")
        @NotNull(message = "Passport number cannot be null")
        @Pattern(regexp = "^[0-9]{6}$", message = "Passport number must be 6 digits")
        String passportNumber
) implements Serializable {
    @JsonIgnore
    @AssertTrue(message = "Borrower must be at least 18 years old")
    public boolean isAgeValid() {
        if (birthdate == null) {
            return false;
        }
        return Period.between(birthdate, LocalDate.now()).getYears() >= 18;
    }
}
