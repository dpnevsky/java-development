package calculator.exception;

import lombok.Getter;

@Getter
public class LoanCheckException extends RuntimeException {
    private final String details;

    public LoanCheckException(String details) {
        super(details);
        this.details = details;
    }
}
