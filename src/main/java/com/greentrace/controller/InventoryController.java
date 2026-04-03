package com.greentrace.controller;

import com.greentrace.dto.FridgeItemRequest;
import com.greentrace.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addItem(@PathVariable Long userId,
                                     @RequestBody FridgeItemRequest request) {
        try {
            inventoryService.addItem(userId, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Item added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInventory(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(inventoryService.getUserInventory(userId));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId) {
        try {
            inventoryService.removeItem(itemId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Item removed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/expiring/{userId}")
    public ResponseEntity<?> getExpiringItems(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(inventoryService.getExpiringItems(userId));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<?> getInventoryStats(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(inventoryService.getInventoryStats(userId));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}