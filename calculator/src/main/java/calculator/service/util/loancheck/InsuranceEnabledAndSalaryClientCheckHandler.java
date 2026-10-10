package calculator.service.util.loancheck;

import calculator.exception.LoanCheckException;
import core.dto.ScoringDataDto;
import org.springframework.stereotype.Component;
import calculator.service.util.Constant;

import java.math.BigDecimal;

@Component
public class InsuranceEnabledAndSalaryClientCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        if (scoringData.isInsuranceEnabled()) {
            rate = rate.subtract(Constant.DISCOUNT_FOR_INSURANCE);
        }
        if (scoringData.isSalaryClient()) {
            rate = rate.subtract(Constant.DISCOUNT_FOR_SALARY_CLIENT);
        }
        return rate;
    }
}
