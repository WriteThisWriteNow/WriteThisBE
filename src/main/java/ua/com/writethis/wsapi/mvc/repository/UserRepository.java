package ua.com.writethis.wsapi.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.writethis.wsapi.mvc.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
