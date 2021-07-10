package com.cc.glovobe.controller;

import com.cc.glovobe.dto.MealDto;
import com.cc.glovobe.exception.ExceptionHandling;
import com.cc.glovobe.exception.domain.FavoriteMealNotFoundException;
import com.cc.glovobe.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/meal")
@CrossOrigin(origins = "http://localhost:3000")
public class MealController extends ExceptionHandling {

    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }


    @GetMapping("/prices")
    public ResponseEntity<Set<MealDto>> getAllMealPrices() {
        Set<MealDto> meals = mealService.getMeals();
        return new ResponseEntity<>(meals, OK);
    }

    @GetMapping("/prices/category/{category}")
    public ResponseEntity<Set<MealDto>> getMealByCategory(@PathVariable String category) {
        Set<MealDto> meals = mealService.getMealByCategory(category);
        return new ResponseEntity<>(meals, OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Integer> mealPrice(@PathVariable Long id) throws FavoriteMealNotFoundException {
        Integer mealPrice = mealService.getById(id);
        return new ResponseEntity<>(mealPrice, OK);
    }

}
