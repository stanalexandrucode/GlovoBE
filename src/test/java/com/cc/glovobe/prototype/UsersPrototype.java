package com.cc.glovobe.prototype;

import com.cc.glovobe.model.User;

import static com.cc.glovobe.enumeration.Role.ROLE_USER;

public class UsersPrototype {

    public static User aUser() {
        String email = "nelu@gmail.com";
        User user = new User();
        user.setId(1L);
        user.setFirstName("Alexandru");
        user.setLastName("Stan");
        user.setEmail(email);
        user.setPassword("password");
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setIsNonLocked(true);
        user.setEnabled(false);

        return user;

    }
}
