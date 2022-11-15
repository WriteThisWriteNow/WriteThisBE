package ua.com.writethis.wsapi.config.security;

import org.junit.jupiter.api.Test;
import ua.com.writethis.wsapi.mail.EmailVerificationToken;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationTokenTest {

    private final EmailVerificationToken TOKEN = new EmailVerificationToken("test@mail.com", 1);

    @Test
    void encode() {
        // when
        String encodedToken = TOKEN.encode();

        // then
        assertThat(encodedToken).endsWith("=");
    }

    @Test
    void decode() {
        // given
        String encodedToken = TOKEN.encode();

        // when
        EmailVerificationToken decodedToken = EmailVerificationToken.decode(encodedToken);

        // then
        assertThat(decodedToken).isEqualTo(TOKEN);
    }

    @Test
    void isActive() {
        // given
        EmailVerificationToken expiredToken = new EmailVerificationToken("test@mail.com", -5);
        EmailVerificationToken nonExpiredToken = new EmailVerificationToken("test@mail.com", 5);

        // then
        assertThat(expiredToken.isActive()).isFalse();
        assertThat(nonExpiredToken.isActive()).isTrue();
    }
}