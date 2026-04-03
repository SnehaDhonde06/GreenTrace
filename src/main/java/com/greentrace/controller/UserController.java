package com.greentrace.controller;

import com.greentrace.dto.UpdateProfileRequest;
import com.greentrace.model.User;
import com.greentrace.model.UserStats;
import com.greentrace.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")  // NOT "/api/users" because context-path already adds /api
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("fullName", user.getFullName());
            response.put("phone", user.getPhone());
            response.put("location", user.getLocation());
            response.put("profilePic", user.getProfilePic());
            response.put("skillLevel", user.getSkillLevel());
            response.put("bio", user.getBio());
            response.put("dietaryPreferences", user.getDietaryPreferencesAsList());
            response.put("createdAt", user.getCreatedAt());

            if (user.getStats() != null) {
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalRecipesSaved", user.getStats().getTotalRecipesSaved());
                stats.put("totalRecipesCooked", user.getStats().getTotalRecipesCooked());
                stats.put("totalReviews", user.getStats().getTotalReviews());
                stats.put("wastePreventedKg", user.getStats().getWastePreventedKg());
                stats.put("moneySaved", user.getStats().getMoneySaved());
                response.put("stats", stats);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId,
                                           @RequestBody UpdateProfileRequest request) {
        try {
            Map<String, Object> response = userService.updateProfile(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{userId}/profile-pic")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable Long userId,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = userService.uploadProfilePicture(userId, file);
            Map<String, String> response = new HashMap<>();
            response.put("profilePic", imageUrl);
            response.put("message", "Profile picture updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable Long userId) {
        try {
            UserStats stats = userService.getUserStats(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("totalRecipesSaved", stats.getTotalRecipesSaved());
            response.put("totalRecipesCooked", stats.getTotalRecipesCooked());
            response.put("totalReviews", stats.getTotalReviews());
            response.put("wastePreventedKg", stats.getWastePreventedKg());
            response.put("moneySaved", stats.getMoneySaved());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{userId}/dietary")
    public ResponseEntity<?> getDietaryPreferences(@PathVariable Long userId) {
        try {
            List<String> dietary = userService.getDietaryPreferences(userId);
            Map<String, List<String>> response = new HashMap<>();
            response.put("dietaryPreferences", dietary);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{userId}/skill")
    public ResponseEntity<?> getSkillLevel(@PathVariable Long userId) {
        try {
            String skillLevel = userService.getSkillLevel(userId);
            Map<String, String> response = new HashMap<>();
            response.put("skillLevel", skillLevel);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{userId}/bio")
    public ResponseEntity<?> getUserBio(@PathVariable Long userId) {
        try {
            String bio = userService.getUserBio(userId);
            Map<String, String> response = new HashMap<>();
            response.put("bio", bio);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{userId}/location")
    public ResponseEntity<?> getUserLocation(@PathVariable Long userId) {
        try {
            String location = userService.getUserLocation(userId);
            Map<String, String> response = new HashMap<>();
            response.put("location", location);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}