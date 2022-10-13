package ua.com.writethis.wsapi.mvc.services.impl;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.writethis.wsapi.config.security.EmailVerificationToken;
import ua.com.writethis.wsapi.exception.AlreadyExistsException;
import ua.com.writethis.wsapi.exception.PasswordsDontMatchException;
import ua.com.writethis.wsapi.exception.RecordNotFoundException;
import ua.com.writethis.wsapi.mvc.dto.RegistrationDTO;
import ua.com.writethis.wsapi.mvc.dto.UserDTO;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;
import ua.com.writethis.wsapi.mvc.services.UserService;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ENTITY = "User";
    private static final String EMAIL_IDENTIFIER = "email";
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${wsapi.email-verification-token.expirationMinutes}")
    private int emailVerificationTokenExpirationMinutes;
    @Value("${wsapi.domain.root}")
    private String rootDomain;
    @Value("${wsapi.host.address}")
    private String hostAddress;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException(USER_ENTITY, EMAIL_IDENTIFIER, email));
        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password(user.getPassword())
                .disabled(user.isDisabled())
                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .build();
    }

    @Override
    public UserDTO registerUser(RegistrationDTO registrationDTO) {
        EmailVerificationToken token = new EmailVerificationToken(registrationDTO.getEmail(), emailVerificationTokenExpirationMinutes);
        User userToSave;

        if (!registrationDTO.getPassword().equals(registrationDTO.getMatchingPassword()))
            throw new PasswordsDontMatchException();
        if (userRepository.existsByEmail(registrationDTO.getEmail())){
            userToSave = userRepository.findByEmail(registrationDTO.getEmail()).orElseThrow();
            if (userToSave.isDisabled()) {
                userToSave.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
                userToSave.setEmailVerificationToken(token.encode());
                userToSave.setCreationDate(LocalDate.now());
            } else {
                throw new AlreadyExistsException("User", EMAIL_IDENTIFIER, registrationDTO.getEmail());
            }

        }

        final String READER_ROLE_NAME = "READER";
        Role readerRole = roleRepository.findByName(READER_ROLE_NAME).orElseThrow(() -> new RecordNotFoundException("Role", "name", READER_ROLE_NAME));

        userToSave = User.builder()
                .creationDate(LocalDate.now())
                .emailVerificationToken(token.encode())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .roles(Sets.newHashSet(readerRole))
                .build();

        User savedUser = userRepository.save(userToSave);
        sendEmailVerificationEmail(savedUser.getEmail(), savedUser.getEmailVerificationToken());
        return new ModelMapper().map(savedUser, UserDTO.class);
    }

//    TODO: Bring it to separate class
    @SneakyThrows
    private void sendEmailVerificationEmail(String email, String token) {
        String html = """
                <h3>Verify Your information</h3>
                <div>Please, click the <a href="[[HOST]]/register/confirm?token=[[TOKEN]]">link</a> to confirm your email</div>
                """
                .replace("[[HOST]]", hostAddress)
                .replace("[[TOKEN]]", token);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("\"Write this\" email confirmation");
        helper.setFrom("verification@" + rootDomain);
        helper.setTo(email);

        boolean isHtml = true;
        helper.setText(html, isHtml);

        mailSender.send(message);
    }
}
