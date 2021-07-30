package com.cc.glovobe.service;

import com.cc.glovobe.dto.FavoriteDto;
import com.cc.glovobe.dto.MealDto;
import com.cc.glovobe.embededId.FavoriteId;
import com.cc.glovobe.exception.domain.FavoriteMealNotFoundException;
import com.cc.glovobe.exception.domain.MealNotFoundException;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.mapper.MealMapper;
import com.cc.glovobe.model.Favorite;
import com.cc.glovobe.model.Meal;
import com.cc.glovobe.model.User;
import com.cc.glovobe.repository.FavoriteRepository;
import com.cc.glovobe.repository.MealRepository;
import com.cc.glovobe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Service
public class FavoriteService {
    public static final String PRINCIPAL_NOT_FOUND_WITH_EMAIL = "Principal not found with email: ";
    public static final String MEAL_NOT_FOUND_WITH_ID = "Meal not found with id: ";
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final FavoriteRepository favoriteRepository;
    private final MealMapper mealMapper;

    @Autowired
    public FavoriteService(UserRepository userRepository, MealRepository mealRepository, FavoriteRepository favoriteRepository, MealMapper mealMapper) {
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
        this.favoriteRepository = favoriteRepository;
        this.mealMapper = mealMapper;
    }

    @Transactional
    public Favorite addFavMeal(FavoriteDto favoriteDto, String principalEmail) throws UserNotFoundException, MealNotFoundException {
        User user = getUserByEmail(principalEmail);

        Meal meal = getMealById(favoriteDto.getMealId());

        FavoriteId favoriteId = new FavoriteId();
        favoriteId.setUserId(user.getId());
        favoriteId.setMealId(meal.getId());

        Favorite favorite = new Favorite();
        favorite.setMeal(meal);
        favorite.setUser(user);
        favorite.setId(favoriteId);
        favorite.setCreatedAt(LocalDateTime.now());
        favoriteRepository.save(favorite);
        return favorite;

    }

    @Transactional
    public void deleteFavMealById(Long id, String principalEmail) throws MealNotFoundException {
        User user = userRepository.findUserByEmail(principalEmail);
        Meal meal = getMealById(id);

        FavoriteId favoriteId = new FavoriteId();
        favoriteId.setUserId(user.getId());
        favoriteId.setMealId(meal.getId());
        favoriteRepository.deleteFavoriteById(favoriteId);

    }

    @Transactional
    public User getUserByEmail(String principalEmail) throws UserNotFoundException {
        User user = userRepository.findUserByEmail(principalEmail);
        if (user == null) {
            throw new UserNotFoundException(PRINCIPAL_NOT_FOUND_WITH_EMAIL + principalEmail);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<MealDto> getAllFavoriteMeals(String principal) throws UserNotFoundException {
        User user = getUserByEmail(principal);
        List<Favorite> favorites = favoriteRepository.findFavoritesByUser(user);
        return favorites
                .stream()
                .map(mealMapper::mealToMealDto)
                .collect(Collectors.toList());
    }

    Meal getMealById(Long id) throws MealNotFoundException {
        Meal meal = mealRepository.findMealById(id);
        if (meal == null) {
            throw new MealNotFoundException(MEAL_NOT_FOUND_WITH_ID + id);
        }
        return meal;
    }
}
