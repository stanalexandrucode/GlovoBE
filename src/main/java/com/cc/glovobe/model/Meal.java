package com.cc.glovobe.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Meal")
@Table(name = "meal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Meal {

    @Id
    @SequenceGenerator(
            name = "meal_sequence",
            sequenceName = "meal_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "meal_sequence"
    )
    @Column(name = "id")
    private Long id;

    @Column(
            name = "price",
            nullable = false,
            columnDefinition = "Integer",
            updatable = false
    )
    private Integer price;

    private String category;


    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "meal"
    )
    private List<Favorite> favorites = new ArrayList<>();


    public Meal(Long id, Integer price) {
        this.id = id;
        this.price = price;
    }


    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

}
