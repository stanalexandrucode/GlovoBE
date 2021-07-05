package com.cc.glovobe.repository;

import com.cc.glovobe.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Meal findMealById(Long id);

    List<Meal> findAll();
}
