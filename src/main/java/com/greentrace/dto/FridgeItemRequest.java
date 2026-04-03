package com.greentrace.dto;

import java.time.LocalDate;

public class FridgeItemRequest {
    private String ingredientName;
    private Double quantity;
    private String unit;
    private LocalDate storageDate;
    private String storageLocation;

    // Constructors
    public FridgeItemRequest() {}

    // Getters and Setters
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getStorageDate() { return storageDate; }
    public void setStorageDate(LocalDate storageDate) { this.storageDate = storageDate; }

    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
}