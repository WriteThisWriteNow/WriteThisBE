package ua.com.writethis.wsapi.mvc.services.impl;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.writethis.wsapi.config.security.EmailVerificationToken;
import ua.com.writethis.wsapi.exception.AlreadyExistsException;
import ua.com.writethis.wsapi.exception.EmailVerificationException;
import ua.com.writethis.wsapi.exception.EmailVerificationExpiredException;
import ua.com.writethis.wsapi.exception.PasswordsDontMatchException;
import ua.com.writethis.wsapi.exception.RecordNotFoundException;
import ua.com.writethis.wsapi.mail.WsMailSender;
import ua.com.writethis.wsapi.mvc.dto.RegistrationDTO;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;
import ua.com.writethis.wsapi.mvc.services.UserService;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ENTITY = "User";
    private static final String EMAIL_IDENTIFIER = "email";

    private final WsMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${wsapi.emailVerification.token.expirationMinutes}")
    private int emailVerificationTokenExpirationMinutes;

    private static void validate(EmailVerificationToken decodedToken) {
        if (!decodedToken.isActive()) throw new EmailVerificationExpiredException();
    }

    private static void activateUser(EmailVerificationToken decodedToken, User user) {
        if (!user.getEmail().equals(decodedToken.getEmail())) throw new EmailVerificationException();
        user.setDisabled(false);
        user.setEmailVerificationToken(null);
    }

    private static boolean passwordsDontMatch(RegistrationDTO registrationDTO) {
        return !registrationDTO.getPassword().equals(registrationDTO.getMatchingPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDisabledIsFalse(email).orElseThrow(() -> new RecordNotFoundException(USER_ENTITY, EMAIL_IDENTIFIER, email));
        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password(user.getPassword())
                .disabled(user.isDisabled())
                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .build();
    }

    @Transactional
    @Override
    public void registerUser(RegistrationDTO registrationDTO) {
        String token = new EmailVerificationToken(registrationDTO.getEmail(), emailVerificationTokenExpirationMinutes).encode();
        User user;
        if (passwordsDontMatch(registrationDTO)) {
            throw new PasswordsDontMatchException();
        }
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            user = userRepository.findByEmail(registrationDTO.getEmail()).orElseThrow();
            if (user.isDisabled()) {
                user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
                user.setEmailVerificationToken(token);
                user.setCreationDate(LocalDate.now());
            } else {
                throw new AlreadyExistsException("User", EMAIL_IDENTIFIER, registrationDTO.getEmail());
            }
        } else {
            final String READER_ROLE_NAME = "READER";
            Role readerRole = roleRepository.findByName(READER_ROLE_NAME).orElseThrow(() -> new RecordNotFoundException("Role", "name", READER_ROLE_NAME));

            user = User.builder()
                    .disabled(true)
                    .creationDate(LocalDate.now())
                    .emailVerificationToken(token)
                    .email(registrationDTO.getEmail())
                    .password(passwordEncoder.encode(registrationDTO.getPassword()))
                    .roles(Sets.newHashSet(readerRole))
                    .build();
        }

        userRepository.save(user);
        mailSender.sendEmailVerificationEmail(user.getEmail(), token);
    }

    @Override
    @Transactional
    public void confirmRegistration(String token) {
        EmailVerificationToken decodedToken = EmailVerificationToken.decode(token);
        validate(decodedToken);
        User user = userRepository.findByEmailVerificationToken(token).orElseThrow(EmailVerificationExpiredException::new);
        activateUser(decodedToken, user);
    }
}
