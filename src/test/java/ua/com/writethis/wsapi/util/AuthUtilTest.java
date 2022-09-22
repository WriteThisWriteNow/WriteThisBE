package ua.com.writethis.wsapi.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.writethis.wsapi.config.security.jwt.JwtConfig;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ExtendWith(MockitoExtension.class)
class AuthUtilTest {

    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private JwtConfig jwtConfig;

    @Test
    void setAuthorizationErrorResponse() throws IOException {
        //given
        final String MESSAGE = "Test exception message";
        Exception exception = new Exception(MESSAGE);

        //when
        when(response.getOutputStream()).thenReturn(outputStream);

        AuthUtil.setAuthorizationErrorResponse(response, exception);

        //then
        verify(response).setHeader("error", exception.getMessage());
        verify(response).setStatus(FORBIDDEN.value());
        verify(response).setContentType(APPLICATION_JSON_VALUE);
    }

    @Test
    void verifyToken() {
        //given
        final String SECRET_KEY = "test";
        final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwibmFtZSI6InRlc3QiLCJpYXQiOjE2MDAwMDAwMDB9.Py7RIwFE5micizDE0q3nlLHOOvQMnJJAL9r_4zr7XnM";

        //when
        when(jwtConfig.getSecretKey()).thenReturn(SECRET_KEY);

        DecodedJWT decodedJWT = AuthUtil.verifyToken(TOKEN, jwtConfig);
        //then

        assertThat(decodedJWT).isNotNull();
    }

    @Test
    void sneakyThrows() throws IOException {
        //given
        Exception exception = new Exception("Test exception message");

        //when
        when(response.getOutputStream()).thenThrow(new IOException());

        //then
        assertThrows(IOException.class, () -> AuthUtil.setAuthorizationErrorResponse(response, exception));
    }
}