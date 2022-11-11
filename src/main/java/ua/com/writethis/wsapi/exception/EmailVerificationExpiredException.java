package ua.com.writethis.wsapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailVerificationExpiredException extends RuntimeException {
    private static final String MESSAGE = "Verification token invalid or had been expired";

    public EmailVerificationExpiredException() {
        super(MESSAGE);
    }
}
