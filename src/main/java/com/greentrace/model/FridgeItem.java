package com.greentrace.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fridge_inventory")
public class FridgeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ingredient_name", nullable = false, length = 100)
    private String ingredientName;

    private Double quantity;

    @Column(length = 20)
    private String unit;

    @Column(name = "storage_date", nullable = false)
    private LocalDate storageDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "storage_location", length = 50)
    private String storageLocation;

    @Column(length = 20)
    private String status = "active";

    @Column(name = "added_date")
    private LocalDate addedDate;

    // Constructors
    public FridgeItem() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getStorageDate() { return storageDate; }
    public void setStorageDate(LocalDate storageDate) { this.storageDate = storageDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }
}