package ua.com.writethis.wsapi.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncodeEmailVerificationTokenExceptionTest {

    @Test
    void shouldHaveCorrectMessage() {
        // given
        final String MESSAGE = "Unable to encode email-verification token";

        EncodeEmailVerificationTokenException exception = assertThrows(EncodeEmailVerificationTokenException.class, () -> {
            throw new EncodeEmailVerificationTokenException();
        });

        assertThat(exception.getMessage()).isEqualTo(MESSAGE);
    }
}