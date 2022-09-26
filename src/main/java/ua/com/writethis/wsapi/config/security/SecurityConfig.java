package ua.com.writethis.wsapi.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.com.writethis.wsapi.config.security.jwt.JwtConfig;
import ua.com.writethis.wsapi.filter.CustomAuthenticationFilter;
import ua.com.writethis.wsapi.filter.CustomAuthorizationFilter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static ua.com.writethis.wsapi.config.security.AuthList.SWAGGER;
import static ua.com.writethis.wsapi.config.security.AuthList.WHITE_LIST;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtConfig jwtConfig;

    @Value("${wsapi.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(WHITE_LIST.get()).permitAll()
                .antMatchers(SWAGGER.get()).permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(new CustomAuthorizationFilter(jwtConfig), CustomAuthenticationFilter.class)
                .addFilter(new CustomAuthenticationFilter(authenticationManager(), jwtConfig));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods(GET.name(), POST.name(), PUT.name(), DELETE.name())
                        .allowedOrigins(allowedOrigins)
                        .allowedHeaders(AUTHORIZATION, CACHE_CONTROL, CONTENT_TYPE)
                        .allowCredentials(true)
                        .maxAge(300);
            }
        };
    }
}
