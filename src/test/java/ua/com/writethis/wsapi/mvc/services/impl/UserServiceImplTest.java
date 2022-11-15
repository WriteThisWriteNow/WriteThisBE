package ua.com.writethis.wsapi.mvc.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ua.com.writethis.wsapi.exception.AlreadyExistsException;
import ua.com.writethis.wsapi.exception.EmailVerificationException;
import ua.com.writethis.wsapi.exception.EmailVerificationExpiredException;
import ua.com.writethis.wsapi.exception.PasswordsDontMatchException;
import ua.com.writethis.wsapi.exception.RecordNotFoundException;
import ua.com.writethis.wsapi.mail.EmailVerificationToken;
import ua.com.writethis.wsapi.mail.WsMailSender;
import ua.com.writethis.wsapi.mvc.dto.RegistrationDTO;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private WsMailSender mailSender;
    @Mock
    private PasswordEncoder passwordEncoder;
    private RegistrationDTO registrationDTO = new RegistrationDTO(EMAIL, PASSWORD, PASSWORD);
    private String encodedToken;
    private User user;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(userService, "emailVerificationTokenExpirationMinutes", 15);
        encodedToken = new EmailVerificationToken(EMAIL, 5).encode();
        user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .roles(new HashSet<>())
                .disabled(false)
                .emailVerificationToken(encodedToken)
                .build();
        user.setId(1L);
    }

    @Test
    void loadUserByUsername() {
        //when
        when(userRepository.findByEmailAndDisabledIsFalse(EMAIL)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(EMAIL);

        //then
        assertThat(userDetails).isNotNull();
    }

    @Test
    void loadUserByUsername_shouldThrowRecordNotFoundException() {
        //when
        when(userRepository.findByEmailAndDisabledIsFalse(EMAIL)).thenReturn(Optional.empty());

        //then
        assertThrows(RecordNotFoundException.class, () -> userService.loadUserByUsername(EMAIL));
    }

    @Test
    void confirmRegistration() {
        // given
        user.setDisabled(true);

        // when
        when(userRepository.findByEmailVerificationToken(encodedToken)).thenReturn(Optional.of(user));

        userService.confirmRegistration(encodedToken);

        // then
        assertThat(user.isDisabled()).isFalse();
        assertThat(user.getEmailVerificationToken()).isNull();
    }

    @Test
    void confirmExpiredRegistration_shouldThrowExpiredException() {
        // given
        encodedToken = new EmailVerificationToken(EMAIL, -5).encode();

        // then
        assertThrows(EmailVerificationExpiredException.class, () -> userService.confirmRegistration(encodedToken));
    }

    @Test
    void confirmExpiredRegistration_shouldThrowVerificationException() {
        // given
        user.setEmail("wrong@email");

        // when
        when(userRepository.findByEmailVerificationToken(encodedToken)).thenReturn(Optional.of(user));

        // then
        assertThrows(EmailVerificationException.class, () -> userService.confirmRegistration(encodedToken));
    }

    @Test
    void registerIfUserExists() {
        // given
        user.setDisabled(true);

        // when
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.registerUser(registrationDTO);

        // then
        verify(userRepository).save(any(User.class));
        verify(mailSender).sendEmailVerificationEmail(anyString(), anyString());
    }

    @Test
    void registerIfUserDoesntExist() {
        // given
        final String READER_ROLE_NAME = "READER";

        // when
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role(READER_ROLE_NAME)));

        userService.registerUser(registrationDTO);

        // then
        verify(userRepository).save(any(User.class));
        verify(mailSender).sendEmailVerificationEmail(anyString(), anyString());
    }

    @Test
    void register_shouldThrowPasswordDontMatch() {
        // given
        registrationDTO = new RegistrationDTO(EMAIL, PASSWORD, "differentPassword");

        // then
        assertThrows(PasswordsDontMatchException.class, () -> userService.registerUser(registrationDTO));
    }

    @Test
    void register_shouldThrowExceptionIfUserEnabled() {
        // when
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // then
        assertThrows(AlreadyExistsException.class, () -> userService.registerUser(registrationDTO));
    }

    @Test
    void register_throwIfRoleDoesntExist() {
        // when
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(RecordNotFoundException.class, () -> userService.registerUser(registrationDTO));
    }
}