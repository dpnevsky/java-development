package calculator.service.util.loancheck;

import calculator.exception.LoanCheckException;
import core.dto.ScoringDataDto;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import static calculator.service.util.Constant.MIN_FINAL_RATE;

@Service
public class LoanApplicationProcessor {

    private final List<LoanCheckHandler> handlers;

    public LoanApplicationProcessor(List<LoanCheckHandler> handlers) {
        this.handlers = handlers;
    }

    public BigDecimal processLoanApplication(ScoringDataDto scoringData, BigDecimal baseRate) throws LoanCheckException {
        BigDecimal currentRate = baseRate;
        for (LoanCheckHandler handler : handlers) {
            currentRate = handler.handle(scoringData, currentRate);
        }
        return finalCheck(currentRate);
    }

    private BigDecimal finalCheck(BigDecimal rate) {
        if (rate.compareTo(MIN_FINAL_RATE) < 0 ) {
            rate = MIN_FINAL_RATE;
        }
        return rate;
    }
}
