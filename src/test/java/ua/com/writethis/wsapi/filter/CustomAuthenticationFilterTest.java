package ua.com.writethis.wsapi.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ua.com.writethis.wsapi.config.security.jwt.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtConfig jwtConfig;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Authentication authResult;
    @Captor
    private ArgumentCaptor<String> captor;

    @InjectMocks
    private CustomAuthenticationFilter authenticationFilter;

    @Test
    void attemptAuthentication() {
        //when
        authenticationFilter.attemptAuthentication(request, response);

        //verify
        verify(request).getParameter("email");
        verify(request).getParameter("password");
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void successfulAuthentication() {
        //given
        User user = new User("test", "test", List.of(new SimpleGrantedAuthority("test")));
        String TOKEN_START = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.";

        //when
        when(jwtConfig.getTokenPrefix()).thenReturn("Bearer ");
        when(jwtConfig.getTokenExpirationDays()).thenReturn("7");
        when(jwtConfig.getSecretKey()).thenReturn("test");
        when(authResult.getPrincipal()).thenReturn(user);

        authenticationFilter.successfulAuthentication(request, response, filterChain, authResult);

        //then
        verify(authResult).getPrincipal();
        verify(response).setHeader(anyString(), captor.capture());

        assertThat(captor.getValue()).startsWith(TOKEN_START);
    }
}