package ua.com.writethis.wsapi.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.writethis.wsapi.config.security.jwt.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ExtendWith(MockitoExtension.class)
class CustomAuthorizationFilterTest {

    @Mock
    private JwtConfig jwtConfig;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private ServletOutputStream outputStream;
    @Captor
    private ArgumentCaptor<String> captor;
    @InjectMocks
    private CustomAuthorizationFilter customAuthorizationFilter;

    @BeforeEach
    public void setUp() {
        when(request.getServletPath()).thenReturn("/authorization/path");
    }

    @Test
    void doFilterInternal() throws ServletException, IOException {
        //given
        final String TOKEN_HEADER = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
                ".5mhBHqs5_DTLdINd9p5m7ZJ6XD0Xc55kIaCRY5r6HRA";

        //when
        when(request.getHeader(AUTHORIZATION)).thenReturn(TOKEN_HEADER);
        when(jwtConfig.getSecretKey()).thenReturn("test");
        when(jwtConfig.getTokenPrefix()).thenReturn("Bearer ");

        customAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void throwExceptionWithInvalidToken() throws IOException, ServletException {
        final String TOKEN_HEADER = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
                ".u5ClmY6KVIUdReH0H2qpG2oyqrb8VTfJ8NzaLVxylEI";

        //when
        when(request.getHeader(AUTHORIZATION)).thenReturn(TOKEN_HEADER);
        when(jwtConfig.getSecretKey()).thenReturn("test");
        when(jwtConfig.getTokenPrefix()).thenReturn("Bearer ");
        when(response.getOutputStream()).thenReturn(outputStream);

        customAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader(anyString(), captor.capture());
        //then
        assertThat(captor.getValue()).isEqualTo("The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256");
    }

    @Test
    void isAuthenticationRequest() throws ServletException, IOException {
        //when
        when(request.getServletPath()).thenReturn("/login");

        customAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(filterChain).doFilter(request, response);
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "''"
    })
    void authorizationHeaderIsNullOrEmpty(String header) throws ServletException, IOException {
        //when
        when(request.getHeader(AUTHORIZATION)).thenReturn(header);

        customAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //verify
        verify(jwtConfig, times(0)).getSecretKey();
    }

    @Test
    void tokenDoesNotStartWithBearer() throws ServletException, IOException {
        //given
        final String INVALID_TOKEN_HEADER = "invalid";

        //when
        when(jwtConfig.getTokenPrefix()).thenReturn("Bearer ");
        when(request.getHeader(AUTHORIZATION)).thenReturn(INVALID_TOKEN_HEADER);

        customAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //verify
        verify(jwtConfig, times(0)).getSecretKey();
    }
}