package ua.com.writethis.wsapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ResponseStatus(BAD_REQUEST)
public class PasswordsDontMatchException extends RuntimeException {

    private static final String MESSAGE = "Provided passwords don't match";

    public PasswordsDontMatchException() {
        super(MESSAGE);
        log.error(MESSAGE, this);
    }
}
