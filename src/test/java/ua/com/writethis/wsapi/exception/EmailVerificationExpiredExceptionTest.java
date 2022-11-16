package ua.com.writethis.wsapi.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailVerificationExpiredExceptionTest {

    @Test
    void shouldHaveCorrectMessage() {
        // given
        final String MESSAGE = "Verification token invalid or had been expired";

        EmailVerificationExpiredException exception = assertThrows(EmailVerificationExpiredException.class, () -> {
            throw new EmailVerificationExpiredException();
        });

        assertThat(exception.getMessage()).isEqualTo(MESSAGE);
    }
}