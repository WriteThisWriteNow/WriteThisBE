package ua.com.writethis.wsapi.mvc.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ua.com.writethis.wsapi.exception.RecordNotFoundException;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void loadUserByUsername() {
        //given
        final String EMAIL = "email";
        final String PASSWORD = "password";
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(new HashSet<>());

        //when
        when(repository.findByEmailAndDisabledIsFalse(EMAIL)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(EMAIL);

        //then
        assertThat(userDetails).isNotNull();
    }

    @Test
    void loadUserByUsername_shouldThrowRecordNotFoundException() {
        //given
        final String EMAIL = "email";

        //when
        when(repository.findByEmailAndDisabledIsFalse(EMAIL)).thenReturn(Optional.empty());

        //then
        assertThrows(RecordNotFoundException.class, () -> userService.loadUserByUsername(EMAIL));
    }
}