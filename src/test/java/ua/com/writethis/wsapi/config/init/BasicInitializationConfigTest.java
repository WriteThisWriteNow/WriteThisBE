package ua.com.writethis.wsapi.config.init;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.writethis.wsapi.mvc.entity.Role;
import ua.com.writethis.wsapi.mvc.repository.RoleRepository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicInitializationConfigTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private BasicInitializationConfig basicInitializationConfig;

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/BasicRoles.csv")
    void runWhenDoesNotExists(String roleName) {
        //when
        when(roleRepository.existsByName(anyString())).thenReturn(false);
        basicInitializationConfig.run();

        //then
        verify(roleRepository).save(new Role(roleName));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/BasicRoles.csv")
    void runWhenAlreadyExists(String roleName) {
        //when
        when(roleRepository.existsByName(anyString())).thenReturn(true);
        basicInitializationConfig.run();

        //then
        verify(roleRepository, times(0)).save(new Role(roleName));
    }
}