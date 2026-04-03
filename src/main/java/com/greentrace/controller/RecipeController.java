package com.greentrace.controller;

import com.greentrace.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/recipes")  // NOT "/api/recipes"
@CrossOrigin(origins = "*")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/search")
    public ResponseEntity<?> searchRecipes(@RequestParam String ingredients,
                                           @RequestParam(required = false) Long userId) {
        try {
            Object recipes = recipeService.searchRecipes(ingredients, userId);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveRecipe(@RequestParam Long userId,
                                        @RequestParam Long recipeId,
                                        @RequestParam String recipeName,
                                        @RequestParam(required = false) String recipeImage,
                                        @RequestParam(required = false) String cuisineType) {
        try {
            recipeService.saveRecipe(userId, recipeId, recipeName, recipeImage, cuisineType);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Recipe saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/saved/{userId}")
    public ResponseEntity<?> getSavedRecipes(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(recipeService.getSavedRecipes(userId));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/saved/{userId}/{recipeId}")
    public ResponseEntity<?> removeSavedRecipe(@PathVariable Long userId,
                                               @PathVariable Long recipeId) {
        try {
            recipeService.removeSavedRecipe(userId, recipeId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Recipe removed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}