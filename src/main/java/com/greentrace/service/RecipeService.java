package com.greentrace.service;

import com.greentrace.model.SavedRecipe;
import com.greentrace.model.User;
import com.greentrace.model.UserStats;
import com.greentrace.repository.SavedRecipeRepository;
import com.greentrace.repository.UserRepository;
import com.greentrace.repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private SavedRecipeRepository savedRecipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatsRepository userStatsRepository;

    @Value("${spoonacular.api.key}")
    private String apiKey;

    @Value("${spoonacular.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    // Update this method to accept userId
    public Object searchRecipes(String ingredients, Long userId) {
        try {
            String url = String.format("%s/recipes/findByIngredients?ingredients=%s&number=12&ignorePantry=true&ranking=1&apiKey=%s",
                    apiUrl, ingredients, apiKey);

            Object recipes = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();

            // Save search history if userId is provided
            if (userId != null) {
                // You can optionally save search history here
                System.out.println("User " + userId + " searched for: " + ingredients);
            }

            return recipes;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching recipes: " + e.getMessage());
        }
    }

    @Transactional
    public void saveRecipe(Long userId, Long recipeId, String recipeName,
                           String recipeImage, String cuisineType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (savedRecipeRepository.findByUserAndRecipeId(user, recipeId).isPresent()) {
            throw new RuntimeException("Recipe already saved");
        }

        SavedRecipe savedRecipe = new SavedRecipe();
        savedRecipe.setUser(user);
        savedRecipe.setRecipeId(recipeId);
        savedRecipe.setRecipeName(recipeName);
        savedRecipe.setRecipeImage(recipeImage);
        savedRecipe.setCuisineType(cuisineType != null ? cuisineType : "General");
        savedRecipe.setSavedDate(LocalDate.now());

        savedRecipeRepository.save(savedRecipe);

        UserStats stats = user.getStats();
        if (stats != null) {
            stats.setTotalRecipesSaved(stats.getTotalRecipesSaved() + 1);
            userStatsRepository.save(stats);
        }
    }

    public List<SavedRecipe> getSavedRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedRecipeRepository.findByUserOrderBySavedDateDesc(user);
    }

    @Transactional
    public void removeSavedRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        savedRecipeRepository.deleteByUserAndRecipeId(user, recipeId);

        UserStats stats = user.getStats();
        if (stats != null && stats.getTotalRecipesSaved() > 0) {
            stats.setTotalRecipesSaved(stats.getTotalRecipesSaved() - 1);
            userStatsRepository.save(stats);
        }
    }
}