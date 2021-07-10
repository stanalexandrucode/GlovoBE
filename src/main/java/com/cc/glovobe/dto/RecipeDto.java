package com.cc.glovobe.dto;

public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private String userFirstName;
    private Long userId;
    private String image;

    public RecipeDto(Long id, String name, String description, String userFirstName, Long userId, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userFirstName = userFirstName;
        this.userId = userId;
        this.image = image;
    }

    public RecipeDto() {
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

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
