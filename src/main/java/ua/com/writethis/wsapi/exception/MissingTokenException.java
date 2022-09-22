package ua.com.writethis.wsapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MissingTokenException extends RuntimeException {
    public MissingTokenException() {
        super("Refresh token is missing");
    }
}
