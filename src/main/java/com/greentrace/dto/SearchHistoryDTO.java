package com.greentrace.dto;

import java.time.LocalDateTime;

public class SearchHistoryDTO {
    private Long id;
    private String searchQuery;
    private Integer resultCount;
    private LocalDateTime searchedAt;
    private String searchType;
    private String cuisineType;
    private String dietaryFilter;
    private Boolean isSuccessful;

    // Constructors
    public SearchHistoryDTO() {}

    public SearchHistoryDTO(Long id, String searchQuery, Integer resultCount,
                            LocalDateTime searchedAt, String searchType,
                            String cuisineType, String dietaryFilter, Boolean isSuccessful) {
        this.id = id;
        this.searchQuery = searchQuery;
        this.resultCount = resultCount;
        this.searchedAt = searchedAt;
        this.searchType = searchType;
        this.cuisineType = cuisineType;
        this.dietaryFilter = dietaryFilter;
        this.isSuccessful = isSuccessful;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public Integer getResultCount() { return resultCount; }
    public void setResultCount(Integer resultCount) { this.resultCount = resultCount; }

    public LocalDateTime getSearchedAt() { return searchedAt; }
    public void setSearchedAt(LocalDateTime searchedAt) { this.searchedAt = searchedAt; }

    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public String getDietaryFilter() { return dietaryFilter; }
    public void setDietaryFilter(String dietaryFilter) { this.dietaryFilter = dietaryFilter; }

    public Boolean getIsSuccessful() { return isSuccessful; }
    public void setIsSuccessful(Boolean isSuccessful) { this.isSuccessful = isSuccessful; }
}