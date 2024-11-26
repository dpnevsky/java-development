package calculator.service.util.loancheck;

import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;
import org.springframework.stereotype.Component;
import calculator.service.util.Constant;

import java.math.BigDecimal;

@Component
public class SalaryAndAmountCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        BigDecimal salary = scoringData.employment().salary();
        BigDecimal amount = scoringData.amount();

        if (amount.compareTo(salary.multiply(Constant.NUMBER_OF_SALARY)) > 0) {
            throw new LoanCheckException("Loan declined: Requested amount exceeds 24 months of salary.");
        }
        return rate;
    }
}
