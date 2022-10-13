package ua.com.writethis.wsapi.exception;

public class DecodeEmailVerificationTokenException extends RuntimeException {

    private static final String MESSAGE = "Unable to decode email-verification token";

    public DecodeEmailVerificationTokenException() {
        super(MESSAGE);
    }
}
