package com.cc.glovobe.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {
    private Long idMeal;
    private Integer price;
    private String category;
}