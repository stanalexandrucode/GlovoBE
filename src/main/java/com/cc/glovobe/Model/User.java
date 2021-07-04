package com.cc.glovobe.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private String firstName;
    private String lastName;
    @Email(regexp = ".+@.+\\..+")
    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String role; //ROLE_USER{ read, edit }, ROLE_ADMIN {delete}
    private String[] authorities;
    private Boolean isNonLocked;
    private Boolean enabled;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String password, String role, String[] authorities, Boolean isNonLocked, Boolean enabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
        this.isNonLocked = isNonLocked;
        this.enabled = enabled;
    }
}
