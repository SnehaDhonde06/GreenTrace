package com.greentrace.repository;

import com.greentrace.model.FridgeItem;
import com.greentrace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FridgeRepository extends JpaRepository<FridgeItem, Long> {

    List<FridgeItem> findByUserOrderByExpiryDateAsc(User user);

    List<FridgeItem> findByUserAndStatus(User user, String status);

    long countByUserAndStatus(User user, String status);

    // Find expired items
    @Query("SELECT f FROM FridgeItem f WHERE f.user = :user AND f.expiryDate < CURRENT_DATE AND f.status = 'active'")
    List<FridgeItem> findExpiredItems(@Param("user") User user);

    // Find items expiring within 3 days using MySQL DATE_ADD
    @Query(value = "SELECT * FROM fridge_inventory f WHERE f.user_id = :userId AND f.expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND f.status = 'active'",
            nativeQuery = true)
    List<FridgeItem> findExpiringSoon(@Param("userId") Long userId);

    // Helper method to find by user ID
    default List<FridgeItem> findExpiringSoon(User user) {
        return findExpiringSoon(user.getId());
    }
}