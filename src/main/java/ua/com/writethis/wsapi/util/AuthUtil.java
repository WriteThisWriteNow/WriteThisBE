package ua.com.writethis.wsapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.com.writethis.wsapi.config.security.jwt.JwtConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthUtil {

    private AuthUtil() {
    }

    @SneakyThrows
    public static void setAuthorizationErrorResponse(HttpServletResponse response, Exception exception) {
        log.error("Error logging in: {}", exception.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", exception.getMessage());

        response.setHeader("error", exception.getMessage());
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errors);
    }

    public static DecodedJWT verifyToken(String token, JwtConfig jwtConfig) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
