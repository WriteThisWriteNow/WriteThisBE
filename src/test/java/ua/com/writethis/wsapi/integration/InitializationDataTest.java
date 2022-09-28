package ua.com.writethis.wsapi.integration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.entity.User;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;
import ua.com.writethis.wsapi.mvc.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class InitializationDataTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/BasicRoles.csv")
    void basicRolesShouldExist(String roleName) {
        //when
        Role role = roleRepository.findByName(roleName).orElse(null);

        //then
        assertThat(role).isNotNull();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/BasicRoles.csv")
    void basicLocalUsersShouldNotExistForNotLocalProfile(String roleName) {
        //given
        String userEmail = roleName.replace("_", "").toLowerCase();

        //when
        User user = userRepository.findByEmail(userEmail).orElse(null);

        //then
        assertThat(user).isNull();
    }
}
