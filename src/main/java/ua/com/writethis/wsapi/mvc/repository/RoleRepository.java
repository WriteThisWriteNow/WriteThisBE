package ua.com.writethis.wsapi.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.writethis.wsapi.mvc.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    Optional<Role> findByName(String name);
}
