package ua.com.writethis.wsapi.mvc.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.writethis.wsapi.mvc.dto.RegistrationDTO;

public interface UserService extends UserDetailsService {
    void registerUser(RegistrationDTO registrationDTO);

    void confirmRegistration(String token);
}
