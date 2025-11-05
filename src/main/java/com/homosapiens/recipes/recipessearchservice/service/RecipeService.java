package com.homosapiens.recipes.recipessearchservice.service;

import com.homosapiens.recipes.recipessearchservice.model.Recipe;
import org.springframework.lang.NonNull;
import java.util.List;

public interface RecipeService {
    Recipe getRecipeById(@NonNull Long id);
    List<Recipe> searchRecipes(@NonNull String searchTerm);
    void loadRecipesFromExternalApi();
}
