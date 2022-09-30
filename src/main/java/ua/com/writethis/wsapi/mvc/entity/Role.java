package ua.com.writethis.wsapi.mvc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity
public class Role extends BaseEntity {
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
