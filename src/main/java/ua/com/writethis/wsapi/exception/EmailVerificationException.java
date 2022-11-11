package ua.com.writethis.wsapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailVerificationException extends RuntimeException {
    private static final String MESSAGE = "An error occurred while email-token verification";

    public EmailVerificationException() {
        super(MESSAGE);
    }
}
