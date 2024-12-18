package calculator.service.util.loancheck;

import calculator.exception.LoanCheckException;
import core.dto.ScoringDataDto;
import core.type.PositionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmploymentPositionCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        PositionType position = scoringData.employment().position();
        if (position.equals(PositionType.MID_MANAGER)) {
            rate = rate.subtract(BigDecimal.valueOf(2));
        } else if (position.equals(PositionType.TOP_MANAGER)) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }
        return rate;
    }
}
