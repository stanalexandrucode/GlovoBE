package com.cc.glovobe.service;

import com.cc.glovobe.dto.MealDto;
import com.cc.glovobe.exception.domain.FavoriteMealNotFoundException;
import com.cc.glovobe.mapper.MealMapper;
import com.cc.glovobe.model.Meal;
import com.cc.glovobe.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Autowired
    public MealService(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }

    public Set<MealDto> getMeals() {
        return mealRepository.findAll().stream()
                .map(mealMapper::mealToDto)
                .collect(Collectors.toSet());
    }

    public Set<MealDto> getMealByCategory(String category) {
        return mealRepository.findAllByCategory(category).stream()
                .map(mealMapper::mealToDto)
                .collect(Collectors.toSet());
    }

    public Integer getById(Long id) throws FavoriteMealNotFoundException {
        Meal meal = mealRepository.findMealById(id);
        if (meal == null) {
            throw new FavoriteMealNotFoundException("Meal with id not found: " + id);
        }
        return meal.getPrice();
    }
}
