package ua.com.writethis.wsapi.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.com.writethis.wsapi.config.security.jwt.JwtConfig;
import ua.com.writethis.wsapi.util.AuthUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    private DecodedJWT decodedJWT;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (
                !isAuthenticationRequest(request)
                        && !Strings.isNullOrEmpty(authorizationHeader)
                        && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())
        ) {
            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
            try {
                decodedJWT = AuthUtil.verifyToken(token, jwtConfig);
                setTokenInContext();
            } catch (JWTVerificationException e) {
                AuthUtil.setAuthorizationErrorResponse(response, e);
            }
        }

        filterChain.doFilter(request, response);
    }


    private boolean isAuthenticationRequest(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath.equals("/login") || servletPath.equals("/token/refresh");
    }

    private void setTokenInContext() {
        List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim(JwtConfig.CLAIMS_NAME).asList(SimpleGrantedAuthority.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
