package calculator.service.util.loancheck;

import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

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
        return rate;
    }
}
