package com.greentrace.service;

import com.greentrace.dto.LoginRequest;
import com.greentrace.dto.SignupRequest;
import com.greentrace.dto.UpdateProfileRequest;
import com.greentrace.model.User;
import com.greentrace.model.UserStats;
import com.greentrace.repository.UserRepository;
import com.greentrace.repository.UserStatsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class UserService {

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserStatsRepository userStatsRepository) {
        this.userRepository = userRepository;
        this.userStatsRepository = userStatsRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Map<String, Object> register(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName() != null ? request.getFullName() : request.getUsername());
        user.setPhone(request.getPhone());
        user.setLocation(request.getLocation());
        user.setSkillLevel("intermediate");
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        UserStats stats = new UserStats();
        stats.setUser(savedUser);
        stats.setTotalRecipesSaved(0);
        stats.setTotalRecipesCooked(0);
        stats.setTotalReviews(0);
        stats.setWastePreventedKg(0.0);
        stats.setMoneySaved(0.0);
        userStatsRepository.save(stats);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedUser.getId());
        response.put("username", savedUser.getUsername());
        response.put("email", savedUser.getEmail());
        response.put("fullName", savedUser.getFullName());
        response.put("message", "User registered successfully");

        return response;
    }

    // Used by AuthController
    public Map<String, Object> authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("profilePic", user.getProfilePic());
        response.put("message", "Login successful");

        return response;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public Map<String, Object> updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserById(userId);

        // Update fields if they are provided
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }
        if (request.getSkillLevel() != null) {
            user.setSkillLevel(request.getSkillLevel());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        // Handle dietary preferences (List<String> to String)
        if (request.getDietaryPreferences() != null) {
            if (request.getDietaryPreferences().isEmpty()) {
                user.setDietaryPreferences(null);
            } else {
                user.setDietaryPreferences(String.join(",", request.getDietaryPreferences()));
            }
        }

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("phone", user.getPhone());
        response.put("location", user.getLocation());
        response.put("skillLevel", user.getSkillLevel());
        response.put("bio", user.getBio());
        response.put("dietaryPreferences", user.getDietaryPreferences());
        response.put("message", "Profile updated successfully");

        return response;
    }

    @Transactional
    public String uploadProfilePicture(Long userId, MultipartFile file) throws IOException {
        User user = getUserById(userId);

        String uploadDir = "uploads/profile-pics/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(), filePath);

        String imageUrl = "/uploads/profile-pics/" + filename;
        user.setProfilePic(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    public UserStats getUserStats(Long userId) {
        User user = getUserById(userId);
        UserStats stats = user.getStats();
        if (stats == null) {
            stats = new UserStats();
            stats.setUser(user);
            stats = userStatsRepository.save(stats);
        }
        return stats;
    }

    @Transactional
    public void incrementRecipesCooked(Long userId) {
        UserStats stats = getUserStats(userId);
        stats.setTotalRecipesCooked(stats.getTotalRecipesCooked() + 1);
        userStatsRepository.save(stats);
    }

    @Transactional
    public void updateWastePrevented(Long userId, Double wasteKg) {
        UserStats stats = getUserStats(userId);
        double newWaste = stats.getWastePreventedKg() + wasteKg;
        stats.setWastePreventedKg(newWaste);
        stats.setMoneySaved(newWaste * 50);
        userStatsRepository.save(stats);
    }

    @Transactional
    public void incrementReviews(Long userId) {
        UserStats stats = getUserStats(userId);
        stats.setTotalReviews(stats.getTotalReviews() + 1);
        userStatsRepository.save(stats);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserAccount(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    public List<String> getDietaryPreferences(Long userId) {
        User user = getUserById(userId);
        return user.getDietaryPreferencesAsList();
    }

    @Transactional
    public void updateDietaryPreferences(Long userId, List<String> dietaryList) {
        User user = getUserById(userId);
        user.setDietaryPreferencesFromList(dietaryList);
        userRepository.save(user);
    }

    public String getSkillLevel(Long userId) {
        User user = getUserById(userId);
        return user.getSkillLevel();
    }

    @Transactional
    public void updateSkillLevel(Long userId, String skillLevel) {
        User user = getUserById(userId);
        user.setSkillLevel(skillLevel);
        userRepository.save(user);
    }

    public String getUserBio(Long userId) {
        User user = getUserById(userId);
        return user.getBio();
    }

    @Transactional
    public void updateUserBio(Long userId, String bio) {
        User user = getUserById(userId);
        user.setBio(bio);
        userRepository.save(user);
    }

    public String getUserLocation(Long userId) {
        User user = getUserById(userId);
        return user.getLocation();
    }

    @Transactional
    public void updateUserLocation(Long userId, String location) {
        User user = getUserById(userId);
        user.setLocation(location);
        userRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
}