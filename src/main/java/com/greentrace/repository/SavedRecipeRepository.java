package com.greentrace.repository;

import com.greentrace.model.SavedRecipe;
import com.greentrace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository  // ← Add this annotation
public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {
    List<SavedRecipe> findByUserOrderBySavedDateDesc(User user);
    Optional<SavedRecipe> findByUserAndRecipeId(User user, Long recipeId);
    void deleteByUserAndRecipeId(User user, Long recipeId);
    long countByUser(User user);
}