package com.cc.glovobe.model;

import javax.persistence.*;


@Entity
public class Recipe {
    @Id
    @SequenceGenerator(
            name = "recipe_sequence",
            sequenceName = "recipe_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_sequence"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", columnDefinition = "text", length = 10485760)
    private String name;

    @Column(name = "description", columnDefinition = "text", length = 10485760)
    private String description;

    @Column(name = "image")
    private String image;
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_id_fk"
            )
    )
    private User user;

    public Recipe(Long id, String name, String description, String image, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.user = user;
    }

    public Recipe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
