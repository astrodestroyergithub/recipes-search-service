package com.homosapiens.recipes.recipessearchservice.controller;

import com.homosapiens.recipes.recipessearchservice.model.Recipe;
import com.homosapiens.recipes.recipessearchservice.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Test
    void searchRecipes_ReturnsMatchingRecipes() throws Exception {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        List<Recipe> recipes = Arrays.asList(recipe);

        when(recipeService.searchRecipes(anyString())).thenReturn(recipes);

        // Act & Assert
        mockMvc.perform(get("/api/recipes/search")
                .param("query", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Recipe"));
    }

    @Test
    void getRecipeById_ReturnsRecipe() throws Exception {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");

        when(recipeService.getRecipeById(1L)).thenReturn(recipe);

        // Act & Assert
        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Recipe"));
    }
}
