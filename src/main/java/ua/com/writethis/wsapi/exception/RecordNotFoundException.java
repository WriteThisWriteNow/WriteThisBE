package ua.com.writethis.wsapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ResponseStatus(NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String itemType, String identification, Object value) {
        super(String.format("%s with %s - %s not found", itemType, identification, value));
        log.error("{} with {} - {} not found", itemType, identification, value, this);
    }
}
