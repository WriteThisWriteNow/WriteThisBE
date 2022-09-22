package ua.com.writethis.wsapi.mvc.entity;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Role extends BaseEntity {
    private String name;
}
