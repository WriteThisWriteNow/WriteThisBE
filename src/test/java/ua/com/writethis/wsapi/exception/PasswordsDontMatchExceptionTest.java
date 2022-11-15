package ua.com.writethis.wsapi.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordsDontMatchExceptionTest {

    @Test
    void shouldHaveCorrectMessage() {
        // given
        final String MESSAGE = "Provided passwords don't match";

        PasswordsDontMatchException exception = assertThrows(PasswordsDontMatchException.class, () -> {
            throw new PasswordsDontMatchException();
        });

        assertThat(exception.getMessage()).isEqualTo(MESSAGE);
    }
}