package calculator.service.util.loancheck;

import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;
import org.springframework.stereotype.Component;
import calculator.service.util.Constant;

import java.math.BigDecimal;

@Component
public class MaritalStatusCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        switch (scoringData.maritalStatus()) {
            case MARRIED -> rate = rate.subtract(Constant.DISCOUNT_FOR_MARRIED);
            case DIVORCED -> rate = rate.add(Constant.INCREASE_RATE_FOR_DIVORCED);
        }
        return rate;
    }
}
