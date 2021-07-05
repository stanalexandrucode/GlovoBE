package com.cc.glovobe.service;

import com.cc.glovobe.dto.MealDto;
import com.cc.glovobe.mapper.MealMapper;
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

}
