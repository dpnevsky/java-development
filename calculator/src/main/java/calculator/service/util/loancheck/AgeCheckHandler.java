package calculator.service.util.loancheck;

import calculator.exception.LoanCheckException;
import core.dto.ScoringDataDto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import static calculator.service.util.Constant.MAX_AGE_FOR_CREDIT;
import static calculator.service.util.Constant.MIN_AGE_FOR_CREDIT;

@Component
public class AgeCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        int age = LocalDate.now().getYear() - scoringData.birthdate().getYear();
        if (age < MIN_AGE_FOR_CREDIT || age > MAX_AGE_FOR_CREDIT) {
            throw new LoanCheckException("Loan declined: Age not within acceptable range.");
        }
        return rate;
    }
}
