package com.greentrace.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "saved_recipes")
public class SavedRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Column(name = "recipe_name", nullable = false, length = 255)
    private String recipeName;

    @Column(name = "recipe_image", columnDefinition = "TEXT")
    private String recipeImage;

    @Column(name = "cuisine_type", length = 50)
    private String cuisineType;

    @Column(name = "saved_date", nullable = false)
    private LocalDate savedDate;

    @Column(name = "recipe_url", columnDefinition = "TEXT")
    private String recipeUrl;

    // Constructors
    public SavedRecipe() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getRecipeImage() { return recipeImage; }
    public void setRecipeImage(String recipeImage) { this.recipeImage = recipeImage; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public LocalDate getSavedDate() { return savedDate; }
    public void setSavedDate(LocalDate savedDate) { this.savedDate = savedDate; }

    public String getRecipeUrl() { return recipeUrl; }
    public void setRecipeUrl(String recipeUrl) { this.recipeUrl = recipeUrl; }
}