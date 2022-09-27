package ua.com.writethis.wsapi.exception;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlreadyExistsExceptionTest {

    @ParameterizedTest
    @CsvSource({
            "User,id," + 22L,
            "Role name,name,SOME_ROLE",
            ",,"
    })
    void shouldThrowException(String itemType, String identification, Object value) {
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            throw new AlreadyExistsException(itemType, identification, value);
        });

        assertThat(exception.getMessage()).isEqualTo(String.format("%s with %s - %s already exists", itemType, identification, value));
    }
}