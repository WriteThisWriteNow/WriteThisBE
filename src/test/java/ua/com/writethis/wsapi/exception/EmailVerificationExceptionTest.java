package ua.com.writethis.wsapi.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailVerificationExceptionTest {

    @Test
    void shouldHaveCorrectMessage() {
        // given
        final String MESSAGE = "An error occurred while email-token verification";

        EmailVerificationException exception = assertThrows(EmailVerificationException.class, () -> {
            throw new EmailVerificationException();
        });

        assertThat(exception.getMessage()).isEqualTo(MESSAGE);
    }

}