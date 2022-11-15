package ua.com.writethis.wsapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EncodeEmailVerificationTokenException extends RuntimeException {

    private static final String MESSAGE = "Unable to encode email-verification token";

    public EncodeEmailVerificationTokenException() {
        super(MESSAGE);
    }
}
