package com.cc.glovobe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
public class CartDto {
    private Long mealId;
    private Integer quantity;
    private Integer price;

    public CartDto(Long mealId, Integer quantity, Integer price) {
        this.mealId = mealId;
        this.quantity = quantity;
        this.price = price;
    }

    public CartDto() {
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDto cartDto = (CartDto) o;
        return Objects.equals(mealId, cartDto.mealId) && Objects.equals(quantity, cartDto.quantity) && Objects.equals(price, cartDto.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, quantity, price);
    }
}
