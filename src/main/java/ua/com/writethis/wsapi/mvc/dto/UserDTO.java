package ua.com.writethis.wsapi.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull(message = "Email can't be null")
    @Email
    private String email;

    private Set<RoleDTO> roles = new HashSet<>();

    private LocalDate creationDate;
}