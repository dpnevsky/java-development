package calculator.service.util.loancheck;

import calculator.exception.LoanCheckException;
import core.dto.ScoringDataDto;
import core.type.EmploymentStatusType;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import static calculator.service.util.Constant.INCREASE_RATE_FOR_BUSINESS_OWNER;
import static calculator.service.util.Constant.INCREASE_RATE_FOR_SELF_EMPLOYED;

@Component
public class EmploymentStatusCheckHandler implements LoanCheckHandler {
    @Override
    public BigDecimal handle(ScoringDataDto scoringData, BigDecimal rate) throws LoanCheckException {
        EmploymentStatusType status = scoringData.employment().employmentStatus();
        switch (status) {
            case UNEMPLOYED -> throw new LoanCheckException("Loan declined: Unemployed.");
            case SELF_EMPLOYED -> rate = rate.add(INCREASE_RATE_FOR_SELF_EMPLOYED);
            case BUSINESS_OWNER -> rate = rate.add(INCREASE_RATE_FOR_BUSINESS_OWNER);
        }
        return rate;
    }
}
