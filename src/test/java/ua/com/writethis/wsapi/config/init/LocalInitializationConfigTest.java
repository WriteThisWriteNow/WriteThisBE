package ua.com.writethis.wsapi.config.init;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.writethis.wsapi.exception.RecordNotFoundException;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalInitializationConfigTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LocalInitializationConfig config;

    @Test
    void whenUserDoesNotExist() {
        //given
        Role role = new Role("TEST_ROLE");

        //when
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn(role.getName());

        config.run();

        //then
        verify(userRepository, times(5)).save(any(User.class));
    }

    @Test
    void whenUserAlreadyExists() {
        //when
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        config.run();

        //then
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void whenRoleDoesNotExist() {
        //when
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(RecordNotFoundException.class, () -> config.run());
    }
}