package com.cc.glovobe.model;

import com.cc.glovobe.embededId.CartId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;


@Entity(name = "CartItem")
@Table(name = "cart_items")
public class Cart {


    @EmbeddedId
    private CartId id;


    @ManyToOne
    @JsonIgnore
    @MapsId("userId")
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_id_fk"
            )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @MapsId("mealId")
    @JoinColumn(
            name = "meal_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "meal_id_fk"
            )
    )
    private Meal meal;

    @Column(
            name = "qty",
            nullable = false,
            columnDefinition = "Integer")
    private Integer quantity;

    @Column(
            name = "price",
            nullable = false,
            columnDefinition = "Integer",
            updatable = false
    )
    private Integer clientSeenPrice;

    public Cart(CartId id, User user, Meal meal, Integer quantity, Integer clientSeenPrice) {
        this.id = id;
        this.user = user;
        this.meal = meal;
        this.quantity = quantity;
        this.clientSeenPrice = clientSeenPrice;
    }

    public Cart() {
    }

    public CartId getId() {
        return id;
    }

    public void setId(CartId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getClientSeenPrice() {
        return clientSeenPrice;
    }

    public void setClientSeenPrice(Integer clientSeenPrice) {
        this.clientSeenPrice = clientSeenPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(user, cart.user) && Objects.equals(meal, cart.meal) && Objects.equals(quantity, cart.quantity) && Objects.equals(clientSeenPrice, cart.clientSeenPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, meal, quantity, clientSeenPrice);
    }
}
