package com.cc.glovobe.embededId;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class CartId implements Serializable {
    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "meal_id")
    @NotNull
    private Long mealId;

    public CartId(Long userId, Long mealId) {
        this.userId = userId;
        this.mealId = mealId;
    }

    public CartId() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartId cartId = (CartId) o;
        return Objects.equals(userId, cartId.userId) && Objects.equals(mealId, cartId.mealId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, mealId);
    }
}
