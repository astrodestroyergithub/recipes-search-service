package com.homosapiens.recipes.recipessearchservice.repository;

import com.homosapiens.recipes.recipessearchservice.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
