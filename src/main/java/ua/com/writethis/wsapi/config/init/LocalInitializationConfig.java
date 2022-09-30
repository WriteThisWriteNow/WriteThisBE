package ua.com.writethis.wsapi.config.init;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.writethis.wsapi.exception.RecordNotFoundException;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;

import javax.transaction.Transactional;

@Profile("local")
@DependsOn("basicInitializationConfig")
@RequiredArgsConstructor
@Configuration
public class LocalInitializationConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        createUserForRole("SUPER_ADMIN");
        createUserForRole("ADMIN");
        createUserForRole("MANAGER");
        createUserForRole("AUTHOR");
        createUserForRole("READER");
    }

    private void createUserForRole(String roleName) {
        String email = roleName.replace("_", "").toLowerCase();
        if (!userRepository.existsByEmail(email)) {
            Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RecordNotFoundException("Role", "name", roleName));

            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(email));
            user.setRoles(Sets.newHashSet(role));

            userRepository.save(user);
        }
    }
}
