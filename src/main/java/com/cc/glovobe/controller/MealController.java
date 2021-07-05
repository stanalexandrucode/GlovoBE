package com.cc.glovobe.controller;

import com.cc.glovobe.dto.MealDto;
import com.cc.glovobe.exception.ExceptionHandling;
import com.cc.glovobe.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.http.HttpStatus.*;

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


}
