package com.greentrace.service;

import com.greentrace.dto.SearchHistoryDTO;
import com.greentrace.model.SearchHistory;
import com.greentrace.model.User;
import com.greentrace.repository.SearchHistoryRepository;
import com.greentrace.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository,
                                UserRepository userRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveSearch(Long userId, String searchQuery, Integer resultCount,
                           String searchType, String cuisineType, String dietaryFilter,
                           Long responseTimeMs, Boolean isSuccessful) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SearchHistory history = new SearchHistory();
        history.setUser(user);
        history.setSearchQuery(searchQuery);
        history.setResultCount(resultCount);
        history.setSearchType(searchType);
        history.setCuisineType(cuisineType);
        history.setDietaryFilter(dietaryFilter);
        history.setResponseTimeMs(responseTimeMs);
        history.setIsSuccessful(isSuccessful);
        history.setSearchedAt(LocalDateTime.now());

        searchHistoryRepository.save(history);

        // Keep only last 50 searches per user
        List<SearchHistory> allSearches = searchHistoryRepository.findByUserOrderBySearchedAtDesc(user);
        if (allSearches.size() > 50) {
            for (int i = 50; i < allSearches.size(); i++) {
                searchHistoryRepository.delete(allSearches.get(i));
            }
        }
    }

    public List<SearchHistoryDTO> getRecentSearches(Long userId, int limit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SearchHistory> searches = searchHistoryRepository.findTop10ByUserOrderBySearchedAtDesc(user);

        if (limit > 0 && limit < searches.size()) {
            searches = searches.subList(0, limit);
        }

        return searches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SearchHistoryDTO> getSearchesByType(Long userId, String searchType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SearchHistory> searches = searchHistoryRepository.findByUserAndSearchTypeOrderBySearchedAtDesc(user, searchType);

        return searches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SearchHistoryDTO> getSearchesByCuisine(Long userId, String cuisineType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SearchHistory> searches = searchHistoryRepository.findByUserAndCuisineTypeOrderBySearchedAtDesc(user, cuisineType);

        return searches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getSearchSuggestions(Long userId, String prefix) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return searchHistoryRepository.findSearchSuggestions(user, prefix);
    }

    @Transactional
    public void clearSearchHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SearchHistory> searches = searchHistoryRepository.findByUserOrderBySearchedAtDesc(user);
        searchHistoryRepository.deleteAll(searches);
    }

    @Transactional
    public void deleteOldSearches(Long userId, int daysOld) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        searchHistoryRepository.deleteOldSearches(user, cutoffDate);
    }

    // Get popular searches (global)
    public List<Object[]> getPopularSearches() {
        return searchHistoryRepository.findPopularSearches();
    }

    // Get popular searches by cuisine - FIXED METHOD
    public List<Object[]> getPopularSearchesByCuisine(String cuisine) {
        return searchHistoryRepository.findPopularSearchesByCuisine(cuisine);
    }

    // Get popular searches by type
    public List<Object[]> getPopularSearchesByType(String searchType) {
        return searchHistoryRepository.findPopularSearchesByType(searchType);
    }

    // Get popular cuisines
    public List<Object[]> getPopularCuisines() {
        return searchHistoryRepository.findPopularCuisines();
    }

    public SearchStatistics getSearchStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long totalSearches = searchHistoryRepository.countByUser(user);
        List<SearchHistory> recentSearches = searchHistoryRepository.findTop10ByUserOrderBySearchedAtDesc(user);

        SearchStatistics stats = new SearchStatistics();
        stats.setTotalSearches(totalSearches);
        stats.setRecentSearches(recentSearches.size());

        double avgResponseTime = recentSearches.stream()
                .filter(s -> s.getResponseTimeMs() != null)
                .mapToLong(SearchHistory::getResponseTimeMs)
                .average()
                .orElse(0);
        stats.setAverageResponseTimeMs(avgResponseTime);

        long ingredientSearches = searchHistoryRepository.findByUserAndSearchTypeOrderBySearchedAtDesc(user, "ingredient").size();
        long recipeSearches = searchHistoryRepository.findByUserAndSearchTypeOrderBySearchedAtDesc(user, "recipe").size();
        stats.setIngredientSearches(ingredientSearches);
        stats.setRecipeSearches(recipeSearches);

        long successfulSearches = recentSearches.stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsSuccessful()))
                .count();
        stats.setSuccessRate(recentSearches.size() > 0 ? (successfulSearches * 100.0 / recentSearches.size()) : 0);

        return stats;
    }

    private SearchHistoryDTO convertToDTO(SearchHistory history) {
        return new SearchHistoryDTO(
                history.getId(),
                history.getSearchQuery(),
                history.getResultCount(),
                history.getSearchedAt(),
                history.getSearchType(),
                history.getCuisineType(),
                history.getDietaryFilter(),
                history.getIsSuccessful()
        );
    }

    public static class SearchStatistics {
        private long totalSearches;
        private int recentSearches;
        private double averageResponseTimeMs;
        private long ingredientSearches;
        private long recipeSearches;
        private double successRate;

        public long getTotalSearches() { return totalSearches; }
        public void setTotalSearches(long totalSearches) { this.totalSearches = totalSearches; }

        public int getRecentSearches() { return recentSearches; }
        public void setRecentSearches(int recentSearches) { this.recentSearches = recentSearches; }

        public double getAverageResponseTimeMs() { return averageResponseTimeMs; }
        public void setAverageResponseTimeMs(double averageResponseTimeMs) { this.averageResponseTimeMs = averageResponseTimeMs; }

        public long getIngredientSearches() { return ingredientSearches; }
        public void setIngredientSearches(long ingredientSearches) { this.ingredientSearches = ingredientSearches; }

        public long getRecipeSearches() { return recipeSearches; }
        public void setRecipeSearches(long recipeSearches) { this.recipeSearches = recipeSearches; }

        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
    }
}