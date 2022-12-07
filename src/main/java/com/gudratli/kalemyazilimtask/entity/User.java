package com.gudratli.kalemyazilimtask.entity;

import com.gudratli.kalemyazilimtask.enums.RoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity
{
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private RoleType role;
}
