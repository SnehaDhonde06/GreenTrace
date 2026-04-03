package com.greentrace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_stats")
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "total_recipes_saved", columnDefinition = "INT DEFAULT 0")
    private Integer totalRecipesSaved = 0;

    @Column(name = "total_recipes_cooked", columnDefinition = "INT DEFAULT 0")
    private Integer totalRecipesCooked = 0;

    @Column(name = "total_reviews", columnDefinition = "INT DEFAULT 0")
    private Integer totalReviews = 0;

    @Column(name = "waste_prevented_kg", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private Double wastePreventedKg = 0.0;

    @Column(name = "money_saved", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private Double moneySaved = 0.0;

    // Constructors
    public UserStats() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getTotalRecipesSaved() { return totalRecipesSaved; }
    public void setTotalRecipesSaved(Integer totalRecipesSaved) { this.totalRecipesSaved = totalRecipesSaved; }

    public Integer getTotalRecipesCooked() { return totalRecipesCooked; }
    public void setTotalRecipesCooked(Integer totalRecipesCooked) { this.totalRecipesCooked = totalRecipesCooked; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public Double getWastePreventedKg() { return wastePreventedKg; }
    public void setWastePreventedKg(Double wastePreventedKg) { this.wastePreventedKg = wastePreventedKg; }

    public Double getMoneySaved() { return moneySaved; }
    public void setMoneySaved(Double moneySaved) { this.moneySaved = moneySaved; }
}