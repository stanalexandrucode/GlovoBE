package com.cc.glovobe.repository;

import com.cc.glovobe.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.cc.glovobe.enumeration.Role.ROLE_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;


    @Test
    void itShouldCheckIfUserExistsEmail() {
        //given
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
        user.setEnabled(true);
        underTest.save(user);

        //when
        User userTest = underTest.findUserByEmail(email);

        //then
        assertThat(userTest.getFirstName()).isEqualTo("Alexandru");
    }


//    @Test
//    void itShouldEnableUser() {
//        Meal meal = new Meal(1234L, 70);
//        FavoriteId favoriteId = new FavoriteId(11111L, meal.getId());
//        LocalDateTime localDateTime = LocalDateTime.now();
//        List<Favorite> favorites = new ArrayList<>();
//        Favorite favorite = new Favorite(favoriteId, meal, localDateTime);
//        favorites.add(favorite);
//        //given
//        String email = "nelu@gmail.com";
//        User user = new User(
//                11111L,
//                "Ion",
//                "Nelu",
//                email,
//                "123",
//                favorites,
//                ROLE_HR.name(),
//                ROLE_USER.getAuthorities(),
//                true,
//                false
//        );
//        underTest.save(user);
//
//        underTest.enableUser(email);
//
//        assertThat(user.getEnabled()).isTrue();
//    }

    @Test
    void findUserById() {
    }
}
