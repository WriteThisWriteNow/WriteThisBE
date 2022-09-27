package ua.com.writethis.wsapi.mvc.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.writethis.wsapi.exception.RecordNotFoundException;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;
import ua.com.writethis.wsapi.mvc.services.UserService;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ENTITY = "User";
    private static final String EMAIL_IDENTIFIER = "email";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException(USER_ENTITY, EMAIL_IDENTIFIER, email));
        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .build();
    }
}
