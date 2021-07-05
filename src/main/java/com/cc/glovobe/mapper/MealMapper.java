package com.cc.glovobe.mapper;


import com.cc.glovobe.dto.MealDto;
import com.cc.glovobe.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MealMapper {


//    @Mapping(target = "idMeal", expression = "java(favorite.getMeal().getId())")
//    @Mapping(target = "price", expression = "java(favorite.getMeal().getPrice())")
//    MealDto mealToMealDto(Favorite favorite);

    @Mapping(target = "idMeal", source = "id")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "category", source = "category")
    MealDto mealToDto(Meal meal);
}