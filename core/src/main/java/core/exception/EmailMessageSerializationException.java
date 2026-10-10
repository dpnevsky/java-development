package core.exception;

public class EmailMessageSerializationException extends RuntimeException {
    public EmailMessageSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
