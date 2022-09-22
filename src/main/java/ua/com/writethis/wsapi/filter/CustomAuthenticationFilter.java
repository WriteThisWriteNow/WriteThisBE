package ua.com.writethis.wsapi.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.com.writethis.wsapi.config.security.jwt.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static ua.com.writethis.wsapi.config.security.jwt.JwtConfig.CLAIMS_NAME;

@RequiredArgsConstructor
@Transactional
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("email");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        User user = (User) authResult.getPrincipal();
        int tokenExpirationDays = Integer.parseInt(jwtConfig.getTokenExpirationDays());
        response.setHeader(AUTHORIZATION, jwtConfig.getTokenPrefix() + createTokenForDays(tokenExpirationDays, user));
    }

    private String createTokenForDays(int days, User user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
        return JWT.create()
                .withIssuedAt(new java.util.Date())
                .withSubject(user.getUsername())
                .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(days)))
                .withClaim(CLAIMS_NAME, getAuthorities(user))
                .sign(algorithm);
    }

    private List<String> getAuthorities(User user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
