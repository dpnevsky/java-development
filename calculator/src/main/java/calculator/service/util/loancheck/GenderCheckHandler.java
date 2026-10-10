package calculator.service.util.loancheck;

import calculator.exception.LoanCheckException;
import core.dto.ScoringDataDto;
import core.type.GenderType;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import static calculator.service.util.Constant.DISCOUNT_FOR_FEMALE;
import static calculator.service.util.Constant.DISCOUNT_FOR_MALE;
import static calculator.service.util.Constant.FEMALE_MAX_AGE_FOR_DISCOUNT;
import static calculator.service.util.Constant.FEMALE_MIN_AGE_FOR_DISCOUNT;
import static calculator.service.util.Constant.INCREASE_RATE_FOR_NON_BINARY;
import static calculator.service.util.Constant.MALE_MAX_AGE_FOR_DISCOUNT;
import static calculator.service.util.Constant.MALE_MIN_AGE_FOR_DISCOUNT;

@Component
public class GenderCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        GenderType gender = scoringData.gender();
        int age = LocalDate.now().getYear() - scoringData.birthdate().getYear();

        if (gender == GenderType.FEMALE && age >= FEMALE_MIN_AGE_FOR_DISCOUNT && age <= FEMALE_MAX_AGE_FOR_DISCOUNT
        ) {
            rate = rate.subtract(DISCOUNT_FOR_FEMALE);
        } else if (gender == GenderType.MALE && age >= MALE_MIN_AGE_FOR_DISCOUNT && age <= MALE_MAX_AGE_FOR_DISCOUNT) {
            rate = rate.subtract(DISCOUNT_FOR_MALE);
        } else if (gender == GenderType.NON_BINARY) {
            rate = rate.add(INCREASE_RATE_FOR_NON_BINARY);
        }
        return rate;
    }
}
