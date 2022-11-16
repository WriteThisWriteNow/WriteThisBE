package ua.com.writethis.wsapi.mail;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ua.com.writethis.wsapi.exception.DecodeEmailVerificationTokenException;
import ua.com.writethis.wsapi.exception.EncodeEmailVerificationTokenException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Base64;

@ToString
@EqualsAndHashCode
public class EmailVerificationToken implements Serializable {

    @Getter
    private final String email;
    private final LocalDateTime expiresAt;

    public EmailVerificationToken(String email, int expireInMinutes) {
        this.email = email;
        this.expiresAt = LocalDateTime.now().plusMinutes(expireInMinutes);
    }

    public static EmailVerificationToken decode(String encodedToken) {
        try {
            byte[] tokenBytes = Base64.getUrlDecoder().decode(encodedToken);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(tokenBytes));
            EmailVerificationToken token = (EmailVerificationToken) ois.readObject();
            ois.close();
            return token;
        } catch (IOException | ClassNotFoundException e) {
            throw new DecodeEmailVerificationTokenException();
        }
    }

    public String encode() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
            return Base64.getUrlEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new EncodeEmailVerificationTokenException();
        }
    }

    public boolean isActive() {
        return expiresAt.isAfter(LocalDateTime.now());
    }
}
