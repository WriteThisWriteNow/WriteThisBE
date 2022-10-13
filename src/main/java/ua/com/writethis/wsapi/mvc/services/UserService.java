package ua.com.writethis.wsapi.mvc.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.writethis.wsapi.mvc.dto.RegistrationDTO;
import ua.com.writethis.wsapi.mvc.dto.UserDTO;

public interface UserService extends UserDetailsService {
    UserDTO registerUser(RegistrationDTO registrationDTO);
}
