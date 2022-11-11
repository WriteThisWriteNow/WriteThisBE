package ua.com.writethis.wsapi.mail;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class WsMailSender {

    private final JavaMailSender javaMailSender;

    @Value("${wsapi.host.address}")
    private String hostAddress;
    @Value("${wsapi.domain.root}")
    private String rootDomain;
    @Value("${wsapi.emailVerification.token.expirationMinutes}")
    private int expirationMinutes;

    @SneakyThrows
    public void sendEmailVerificationEmail(String email, String token) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String until = LocalDateTime.now().plusMinutes(expirationMinutes).format(dateTimeFormatter);
        String html = """
                <h3>Підтвердіть свої дані</h3>
                <div>Будь-ласка, перейдіть за <a href="[[HOST]]/register/confirm?token=[[TOKEN]]">посиланням</a>, щоб підтвердити реєстрацію. Час дії посилання - [[TTL]] хвилин (до [[UNTIL]]).</div>
                """
                .replace("[[HOST]]", hostAddress)
                .replace("[[TOKEN]]", token)
                .replace("[[TTL]]", String.valueOf(expirationMinutes))
                .replace("[[UNTIL]]", until);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("\"Write this\" - підтвердження реєстрації");
        helper.setFrom("verification@" + rootDomain);
        helper.setTo(email);

        boolean isHtml = true;
        helper.setText(html, isHtml);

        javaMailSender.send(message);
    }
}
