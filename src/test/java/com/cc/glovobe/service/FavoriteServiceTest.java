package com.cc.glovobe.service;

import com.cc.glovobe.dto.FavoriteDto;
import com.cc.glovobe.embededId.FavoriteId;
import com.cc.glovobe.exception.domain.MealNotFoundException;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.mapper.MealMapper;
import com.cc.glovobe.model.Favorite;
import com.cc.glovobe.model.Meal;
import com.cc.glovobe.model.User;
import com.cc.glovobe.repository.FavoriteRepository;
import com.cc.glovobe.repository.MealRepository;
import com.cc.glovobe.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.cc.glovobe.enumeration.Role.ROLE_USER;
import static com.cc.glovobe.service.FavoriteService.PRINCIPAL_NOT_FOUND_WITH_EMAIL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MealMapper mealMapper;

    private FavoriteService underTest;


    @BeforeEach
    void setUp() {
        underTest = new FavoriteService(userRepository, mealRepository, favoriteRepository,
                mealMapper);
    }

    @Test
    @Disabled
    void canAddFavMeal() throws UserNotFoundException, MealNotFoundException {
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

        Meal meal = new Meal(1l, 20);
        FavoriteDto favoriteDto = new FavoriteDto(1l);

        FavoriteId favoriteId = new FavoriteId();
        favoriteId.setUserId(user.getId());
        favoriteId.setMealId(meal.getId());

        Favorite favorite = new Favorite();
        favorite.setMeal(meal);
        favorite.setUser(user);
        favorite.setId(favoriteId);
        favorite.setCreatedAt(LocalDateTime.now());


//        given(underTest.getUserByEmail(user.getEmail())).willReturn(user);
//        given(underTest.getMealById(favoriteDto.getMealId())).willReturn(meal);
        favoriteRepository.save(favorite);

         assertThat(underTest.addFavMeal(favoriteDto,email)).isEqualTo(favorite);

    }

    @Test
    @Disabled
    void deleteFavMealById() {
    }

    @Test
    void willThrowWhenGetUserByEmailIsNull() {
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


        //when
        assertThatThrownBy(() -> underTest.getUserByEmail(email))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(PRINCIPAL_NOT_FOUND_WITH_EMAIL + email);
    }

    @Test
    void canGetUserByEmail() {
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

        given(userRepository.findUserByEmail(user.getEmail())).willReturn(user);
        //when
//        underTest.getUserByEmail(email)


    }

    @Test
    @Disabled
    void getAllFavoriteMeals() {
    }
}