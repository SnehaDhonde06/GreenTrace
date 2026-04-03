package com.greentrace.controller;

import com.greentrace.dto.SearchHistoryDTO;
import com.greentrace.service.SearchHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search-history")
@CrossOrigin(origins = "*")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    // Constructor injection
    public SearchHistoryController(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSearch(@RequestParam Long userId,
                                        @RequestParam String searchQuery,
                                        @RequestParam(required = false) Integer resultCount,
                                        @RequestParam(required = false) String searchType,
                                        @RequestParam(required = false) String cuisineType,
                                        @RequestParam(required = false) String dietaryFilter,
                                        @RequestParam(required = false) Long responseTimeMs,
                                        @RequestParam(required = false) Boolean isSuccessful) {
        try {
            searchHistoryService.saveSearch(userId, searchQuery, resultCount,
                    searchType != null ? searchType : "ingredient",
                    cuisineType, dietaryFilter, responseTimeMs,
                    isSuccessful != null ? isSuccessful : true);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Search saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/recent/{userId}")
    public ResponseEntity<?> getRecentSearches(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "10") int limit) {
        try {
            List<SearchHistoryDTO> searches = searchHistoryService.getRecentSearches(userId, limit);
            return ResponseEntity.ok(searches);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/type/{userId}/{searchType}")
    public ResponseEntity<?> getSearchesByType(@PathVariable Long userId,
                                               @PathVariable String searchType) {
        try {
            List<SearchHistoryDTO> searches = searchHistoryService.getSearchesByType(userId, searchType);
            return ResponseEntity.ok(searches);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/cuisine/{userId}/{cuisineType}")
    public ResponseEntity<?> getSearchesByCuisine(@PathVariable Long userId,
                                                  @PathVariable String cuisineType) {
        try {
            List<SearchHistoryDTO> searches = searchHistoryService.getSearchesByCuisine(userId, cuisineType);
            return ResponseEntity.ok(searches);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<?> getSuggestions(@PathVariable Long userId,
                                            @RequestParam String prefix) {
        try {
            List<String> suggestions = searchHistoryService.getSearchSuggestions(userId, prefix);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearHistory(@PathVariable Long userId) {
        try {
            searchHistoryService.clearSearchHistory(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Search history cleared successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<?> getSearchStats(@PathVariable Long userId) {
        try {
            SearchHistoryService.SearchStatistics stats = searchHistoryService.getSearchStatistics(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularSearches() {
        try {
            List<Object[]> popular = searchHistoryService.getPopularSearches();
            return ResponseEntity.ok(popular);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/popular/cuisine")
    public ResponseEntity<?> getPopularSearchesByCuisine(@RequestParam String cuisine) {
        try {
            List<Object[]> popular = searchHistoryService.getPopularSearchesByCuisine(cuisine);
            return ResponseEntity.ok(popular);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}