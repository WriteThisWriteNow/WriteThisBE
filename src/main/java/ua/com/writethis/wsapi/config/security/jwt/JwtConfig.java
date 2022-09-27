package ua.com.writethis.wsapi.config.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "wsapi.jwt")
public class JwtConfig {
    public static final String CLAIMS_NAME = "roles";

    private String secretKey;
    private String tokenPrefix;
    private String tokenExpirationDays;
}
