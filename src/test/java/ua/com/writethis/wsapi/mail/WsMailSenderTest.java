package ua.com.writethis.wsapi.mail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WsMailSenderTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private MimeMessage message;

    @Test
    void sendEmailVerificationEmail() {
        // given
        final String EMAIL = "test@mail.com";
        final String TOKEN = "testtoken";
        WsMailSender wsMailSender = new WsMailSender(javaMailSender);
        ReflectionTestUtils.setField(wsMailSender, "hostAddress", "testhost");
        ReflectionTestUtils.setField(wsMailSender, "expirationMinutes", 5);

        // when
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        wsMailSender.sendEmailVerificationEmail(EMAIL, TOKEN);

        // then
        verify(javaMailSender).send(any(MimeMessage.class));
    }
}