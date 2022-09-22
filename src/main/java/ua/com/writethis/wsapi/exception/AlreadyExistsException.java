package ua.com.writethis.wsapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@ResponseStatus(CONFLICT)
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String itemType, String identification, Object value) {
        super(String.format("%s with %s - %s already exists", itemType, identification, value));
        log.error("{} already exists", itemType, this);
    }
}
