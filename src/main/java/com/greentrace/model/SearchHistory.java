package com.greentrace.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "search_query", nullable = false, length = 255)
    private String searchQuery;

    @Column(name = "result_count")
    private Integer resultCount;

    @Column(name = "searched_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime searchedAt;

    @Column(name = "search_type", length = 50)
    private String searchType;

    @Column(name = "cuisine_type", length = 50)
    private String cuisineType;

    @Column(name = "dietary_filter", length = 50)
    private String dietaryFilter;

    @Column(name = "is_successful")
    private Boolean isSuccessful = true;

    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    // Constructors
    public SearchHistory() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
}