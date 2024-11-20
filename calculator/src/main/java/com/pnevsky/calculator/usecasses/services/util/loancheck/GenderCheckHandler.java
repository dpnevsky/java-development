package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import com.pnevsky.calculator.usecasses.types.GenderType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class GenderCheckHandler extends BaseLoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        GenderType gender = scoringData.gender();
        int age = LocalDate.now().getYear() - scoringData.birthdate().getYear();

        if (gender == GenderType.FEMALE && age >= 32 && age <= 60) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (gender == GenderType.MALE && age >= 30 && age <= 55) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        } else if (gender == GenderType.NON_BINARY) {
            rate = rate.add(BigDecimal.valueOf(7));
        }
        return next(scoringData, rate);
    }
}
