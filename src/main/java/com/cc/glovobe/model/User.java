package com.cc.glovobe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "AppUser")
@Table(name = "user_table",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "email",
                        name = "uk_email_uniq"
                )
        }
)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<Favorite> favorites = new HashSet<>();

    private String role; //ROLE_USER{ read, edit }, ROLE_ADMIN {delete}
    private String[] authorities;
    private Boolean isNonLocked;
    private Boolean isEnabled;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String password, String role, String[] authorities, boolean isNonLocked, boolean isEnabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
        this.isNonLocked = isNonLocked;
        this.isEnabled = isEnabled;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public Boolean getNonLocked() {
        return isNonLocked;
    }

    public void setNonLocked(Boolean nonLocked) {
        isNonLocked = nonLocked;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
