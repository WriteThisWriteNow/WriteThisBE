package ua.com.writethis.wsapi.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.writethis.wsapi.mvc.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
