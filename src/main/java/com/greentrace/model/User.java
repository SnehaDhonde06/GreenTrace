package com.greentrace.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String location;

    @Column(name = "dietary_preferences", columnDefinition = "TEXT")
    private String dietaryPreferences;

    @Column(name = "profile_pic", columnDefinition = "TEXT")
    private String profilePic;

    @Column(name = "skill_level", length = 20)
    private String skillLevel = "intermediate";

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserStats stats;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SavedRecipe> savedRecipes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FridgeItem> fridgeItems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SearchHistory> searchHistory = new ArrayList<>();

    // Constructors
    public User() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(String dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserStats getStats() {
        return stats;
    }

    public void setStats(UserStats stats) {
        this.stats = stats;
    }

    public List<SavedRecipe> getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(List<SavedRecipe> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public List<FridgeItem> getFridgeItems() {
        return fridgeItems;
    }

    public void setFridgeItems(List<FridgeItem> fridgeItems) {
        this.fridgeItems = fridgeItems;
    }

    public List<SearchHistory> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(List<SearchHistory> searchHistory) {
        this.searchHistory = searchHistory;
    }

    // Helper methods
    public List<String> getDietaryPreferencesAsList() {
        if (dietaryPreferences == null || dietaryPreferences.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(dietaryPreferences.split(","));
    }

    public void setDietaryPreferencesFromList(List<String> preferences) {
        if (preferences == null || preferences.isEmpty()) {
            this.dietaryPreferences = null;
        } else {
            this.dietaryPreferences = String.join(",", preferences);
        }
    }
}