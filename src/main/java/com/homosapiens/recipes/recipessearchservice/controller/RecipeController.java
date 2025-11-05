package com.homosapiens.recipes.recipessearchservice.controller;

import com.homosapiens.recipes.recipessearchservice.model.Recipe;
import com.homosapiens.recipes.recipessearchservice.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipe API", description = "API endpoints for recipe search and retrieval")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping("/search")
    @Operation(summary = "Search recipes", description = "Search recipes by name or cuisine using full-text search")
    public ResponseEntity<List<Recipe>> searchRecipes(
            @Parameter(description = "Search term to match against recipe name or cuisine")
            @RequestParam String query) {
        return ResponseEntity.ok(recipeService.searchRecipes(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID", description = "Retrieve a specific recipe by its ID")
    public ResponseEntity<Recipe> getRecipeById(
            @Parameter(description = "ID of the recipe to retrieve")
            @PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }
}
