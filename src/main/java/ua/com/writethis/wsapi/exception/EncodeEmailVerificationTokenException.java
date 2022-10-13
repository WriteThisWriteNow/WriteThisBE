package ua.com.writethis.wsapi.exception;

public class EncodeEmailVerificationTokenException extends RuntimeException {

    private static final String MESSAGE = "Unable to encode email-verification token";

    public EncodeEmailVerificationTokenException() {
        super(MESSAGE);
    }
}
