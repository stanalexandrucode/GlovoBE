package com.cc.glovobe.repository;

import com.cc.glovobe.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.cc.glovobe.prototype.UsersPrototype.aUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }


    @Test
    void itShouldCheckWhenUserIsSaved() {
        //given
        String email = "nelu@gmail.com";
        underTest.save(aUser());

        //when
        User userTest = underTest.findUserByEmail(email);

        //then
        assertThat(userTest.getEmail()).isNotNull().isEqualTo(email);
    }


    @Test
    void itShouldCheckWhenUserSavedIsNotEnabled(){
        //given
        String email = "nelu@gmail.com";
        //when
        underTest.save(aUser());
        User userTest = underTest.findUserByEmail(email);

        //then
        assertThat(userTest.getEnabled()).isNotNull().isFalse();

    }

}
