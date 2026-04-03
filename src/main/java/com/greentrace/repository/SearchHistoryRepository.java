package com.greentrace.repository;

import com.greentrace.model.SearchHistory;
import com.greentrace.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    // ========== BASIC QUERIES ==========

    // Get all searches for a user, ordered by most recent
    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user);

    // Get recent searches (last 10)
    List<SearchHistory> findTop10ByUserOrderBySearchedAtDesc(User user);

    // Get searches by query pattern
    List<SearchHistory> findByUserAndSearchQueryContainingIgnoreCaseOrderBySearchedAtDesc(User user, String query);

    // Get searches by type
    List<SearchHistory> findByUserAndSearchTypeOrderBySearchedAtDesc(User user, String searchType);

    // Get searches by cuisine type
    List<SearchHistory> findByUserAndCuisineTypeOrderBySearchedAtDesc(User user, String cuisineType);

    // Count searches by user
    long countByUser(User user);

    // ========== CUSTOM QUERIES ==========

    // Get search suggestions based on user's history
    @Query("SELECT DISTINCT s.searchQuery FROM SearchHistory s WHERE s.user = :user AND LOWER(s.searchQuery) LIKE LOWER(CONCAT(:prefix, '%')) ORDER BY s.searchedAt DESC")
    List<String> findSearchSuggestions(@Param("user") User user, @Param("prefix") String prefix);

    // Delete old searches
    @Modifying
    @Transactional
    @Query("DELETE FROM SearchHistory s WHERE s.user = :user AND s.searchedAt < :date")
    void deleteOldSearches(@Param("user") User user, @Param("date") LocalDateTime date);

    // Get popular searches (global) - returns [query, count]
    @Query("SELECT s.searchQuery, COUNT(s) as count FROM SearchHistory s GROUP BY s.searchQuery ORDER BY count DESC")
    List<Object[]> findPopularSearches();

    // Get popular searches by cuisine - FIXED METHOD
    @Query("SELECT s.searchQuery, COUNT(s) as count FROM SearchHistory s WHERE s.cuisineType = :cuisine GROUP BY s.searchQuery ORDER BY count DESC")
    List<Object[]> findPopularSearchesByCuisine(@Param("cuisine") String cuisine);

    // Get popular searches by type
    @Query("SELECT s.searchQuery, COUNT(s) as count FROM SearchHistory s WHERE s.searchType = :searchType GROUP BY s.searchQuery ORDER BY count DESC")
    List<Object[]> findPopularSearchesByType(@Param("searchType") String searchType);

    // Get recent searches with pagination
    @Query("SELECT s FROM SearchHistory s WHERE s.user = :user ORDER BY s.searchedAt DESC")
    List<SearchHistory> findRecentSearches(@Param("user") User user, Pageable pageable);

    // Get search count by date range
    @Query("SELECT COUNT(s) FROM SearchHistory s WHERE s.user = :user AND s.searchedAt BETWEEN :startDate AND :endDate")
    long countSearchesByDateRange(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Get most searched cuisine types
    @Query("SELECT s.cuisineType, COUNT(s) as count FROM SearchHistory s WHERE s.cuisineType IS NOT NULL GROUP BY s.cuisineType ORDER BY count DESC")
    List<Object[]> findPopularCuisines();

    // Get searches by successful status
    List<SearchHistory> findByUserAndIsSuccessfulOrderBySearchedAtDesc(User user, Boolean isSuccessful);

    // Get searches with response time > threshold
    @Query("SELECT s FROM SearchHistory s WHERE s.user = :user AND s.responseTimeMs > :threshold ORDER BY s.responseTimeMs DESC")
    List<SearchHistory> findSlowSearches(@Param("user") User user, @Param("threshold") Long threshold);
}