package ua.com.writethis.wsapi.config.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class BasicInitializationConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void run(String... args) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("SUPER_ADMIN"));
        roles.add(new Role("ADMIN"));
        roles.add(new Role("MANAGER"));
        roles.add(new Role("AUTHOR"));
        roles.add(new Role("READER"));

        roles.forEach(role -> {
            if (!roleRepository.existsByName(role.getName()))
                roleRepository.save(role);
        });
    }
}
