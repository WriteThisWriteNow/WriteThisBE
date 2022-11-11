package ua.com.writethis.wsapi.config.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationTokenTest {

    private final EmailVerificationToken TOKEN = new EmailVerificationToken("test@mail.com", 1);

    @Test
    void encode() {
        // given
        final String TOKEN_STARTS_WITH = "rO0ABXNyAD11YS5jb20ud3JpdGV0aGlzLndz";
        // when
        String encodedToken = TOKEN.encode();

        // then
        assertThat(encodedToken).startsWith(TOKEN_STARTS_WITH);
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