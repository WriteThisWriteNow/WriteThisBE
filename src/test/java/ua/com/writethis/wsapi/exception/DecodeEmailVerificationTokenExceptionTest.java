package ua.com.writethis.wsapi.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecodeEmailVerificationTokenExceptionTest {

    @Test
    void shouldHaveCorrectMessage() {
        // given
        final String MESSAGE = "Unable to decode email-verification token";

        DecodeEmailVerificationTokenException exception = assertThrows(DecodeEmailVerificationTokenException.class, () -> {
            throw new DecodeEmailVerificationTokenException();
        });

        assertThat(exception.getMessage()).isEqualTo(MESSAGE);
    }
}