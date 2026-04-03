package com.greentrace.service;

import com.greentrace.dto.FridgeItemRequest;
import com.greentrace.model.FridgeItem;
import com.greentrace.model.User;
import com.greentrace.model.UserStats;
import com.greentrace.repository.FridgeRepository;
import com.greentrace.repository.UserRepository;
import com.greentrace.repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatsRepository userStatsRepository;

    // Food shelf life database (in days)
    private static final Map<String, Integer> SHELF_LIFE = new HashMap<>();

    static {
        SHELF_LIFE.put("tomato", 7);
        SHELF_LIFE.put("tomatoes", 7);
        SHELF_LIFE.put("milk", 7);
        SHELF_LIFE.put("chicken", 3);
        SHELF_LIFE.put("egg", 21);
        SHELF_LIFE.put("eggs", 21);
        SHELF_LIFE.put("onion", 30);
        SHELF_LIFE.put("onions", 30);
        SHELF_LIFE.put("potato", 30);
        SHELF_LIFE.put("potatoes", 30);
        SHELF_LIFE.put("spinach", 5);
        SHELF_LIFE.put("carrot", 14);
        SHELF_LIFE.put("carrots", 14);
        SHELF_LIFE.put("cheese", 30);
        SHELF_LIFE.put("yogurt", 14);
        SHELF_LIFE.put("bread", 5);
        SHELF_LIFE.put("banana", 5);
        SHELF_LIFE.put("bananas", 5);
        SHELF_LIFE.put("apple", 30);
        SHELF_LIFE.put("apples", 30);
        SHELF_LIFE.put("lettuce", 5);
        SHELF_LIFE.put("cucumber", 7);
        SHELF_LIFE.put("bell pepper", 10);
        SHELF_LIFE.put("mushroom", 7);
        SHELF_LIFE.put("mushrooms", 7);
        SHELF_LIFE.put("paneer", 7);
        SHELF_LIFE.put("curd", 7);
        SHELF_LIFE.put("butter", 30);
        SHELF_LIFE.put("cream", 14);
    }

    /**
     * Add an ingredient to user's fridge inventory
     */
    @Transactional
    public void addItem(Long userId, FridgeItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FridgeItem item = new FridgeItem();
        item.setUser(user);
        item.setIngredientName(request.getIngredientName().toLowerCase());
        item.setQuantity(request.getQuantity() != null ? request.getQuantity() : 1.0);
        item.setUnit(request.getUnit() != null ? request.getUnit() : "piece");
        item.setStorageDate(request.getStorageDate() != null ? request.getStorageDate() : LocalDate.now());
        item.setStorageLocation(request.getStorageLocation() != null ? request.getStorageLocation() : "fridge");
        item.setStatus("active");
        item.setAddedDate(LocalDate.now());

        // Calculate expiry date based on shelf life
        Integer shelfLife = SHELF_LIFE.getOrDefault(request.getIngredientName().toLowerCase(), 7);
        item.setExpiryDate(item.getStorageDate().plusDays(shelfLife));

        fridgeRepository.save(item);

        // Update user stats for waste prevention
        updateUserStats(user);
    }

    /**
     * Get all inventory items for a user
     */
    public List<FridgeItem> getUserInventory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return fridgeRepository.findByUserOrderByExpiryDateAsc(user);
    }

    /**
     * Remove an item from inventory
     */
    @Transactional
    public void removeItem(Long itemId) {
        FridgeItem item = fridgeRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Update stats if item was used before expiry
        if (item.getExpiryDate() != null && item.getExpiryDate().isAfter(LocalDate.now())) {
            User user = item.getUser();
            if (user.getStats() != null) {
                double wasteSaved = user.getStats().getWastePreventedKg() + 0.2;
                user.getStats().setWastePreventedKg(wasteSaved);
                user.getStats().setMoneySaved(wasteSaved * 50);
                userStatsRepository.save(user.getStats());
            }
        }

        fridgeRepository.deleteById(itemId);
    }

    /**
     * Get items that are expiring soon (within 3 days)
     */
    public List<FridgeItem> getExpiringItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return fridgeRepository.findExpiringSoon(user);
    }

    /**
     * Get expired items
     */
    public List<FridgeItem> getExpiredItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return fridgeRepository.findExpiredItems(user);
    }

    /**
     * Get inventory statistics for a user
     */
    public Map<String, Object> getInventoryStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long totalItems = fridgeRepository.countByUserAndStatus(user, "active");
        List<FridgeItem> expiringSoon = fridgeRepository.findExpiringSoon(user);
        List<FridgeItem> expired = fridgeRepository.findExpiredItems(user);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalItems", totalItems);
        stats.put("expiringSoon", expiringSoon.size());
        stats.put("expired", expired.size());
        stats.put("expiringItems", expiringSoon);
        stats.put("expiredItems", expired);

        if (user.getStats() != null) {
            stats.put("wastePreventedKg", user.getStats().getWastePreventedKg());
            stats.put("moneySaved", user.getStats().getMoneySaved());
        } else {
            stats.put("wastePreventedKg", 0.0);
            stats.put("moneySaved", 0.0);
        }

        return stats;
    }

    /**
     * Update item quantity
     */
    @Transactional
    public void updateItemQuantity(Long itemId, Double newQuantity) {
        FridgeItem item = fridgeRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(newQuantity);
        fridgeRepository.save(item);
    }

    /**
     * Update user stats based on inventory
     */
    private void updateUserStats(User user) {
        UserStats stats = user.getStats();
        if (stats != null) {
            // Calculate waste prevented based on active items
            long activeItems = fridgeRepository.countByUserAndStatus(user, "active");
            double wasteEstimate = activeItems * 0.15; // 150g per item
            stats.setWastePreventedKg(wasteEstimate);
            stats.setMoneySaved(wasteEstimate * 50);
            userStatsRepository.save(stats);
        }
    }

    /**
     * Calculate days remaining for an item
     */
    public int calculateDaysRemaining(FridgeItem item) {
        if (item.getExpiryDate() == null) {
            return 7; // Default shelf life
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), item.getExpiryDate());
    }

    /**
     * Get shelf life status for an item
     */
    public String getShelfLifeStatus(FridgeItem item) {
        int daysRemaining = calculateDaysRemaining(item);

        if (daysRemaining <= 0) {
            return "EXPIRED";
        } else if (daysRemaining <= 3) {
            return "EXPIRING_SOON";
        } else if (daysRemaining <= 7) {
            return "USE_SOON";
        } else {
            return "FRESH";
        }
    }

    /**
     * Batch add multiple items
     */
    @Transactional
    public void addMultipleItems(Long userId, List<FridgeItemRequest> requests) {
        for (FridgeItemRequest request : requests) {
            addItem(userId, request);
        }
    }

    /**
     * Clear all expired items for a user
     */
    @Transactional
    public int clearExpiredItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FridgeItem> expiredItems = fridgeRepository.findExpiredItems(user);
        int count = expiredItems.size();

        for (FridgeItem item : expiredItems) {
            fridgeRepository.delete(item);
        }

        return count;
    }
}