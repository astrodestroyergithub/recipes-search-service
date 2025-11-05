package com.homosapiens.recipes.recipessearchservice.service;

import com.homosapiens.recipes.recipessearchservice.exception.RecipeNotFoundException;
import com.homosapiens.recipes.recipessearchservice.model.Recipe;
import com.homosapiens.recipes.recipessearchservice.model.RecipesResponse;
import com.homosapiens.recipes.recipessearchservice.repository.RecipeRepository;
import com.homosapiens.recipes.recipessearchservice.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    private static final String EXTERNAL_API_URL = "https://dummyjson.com/recipes";

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EntityManager entityManager;

    @Mock
    private org.springframework.transaction.PlatformTransactionManager transactionManager;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        assertNotNull(transactionManager, "Transaction manager should not be null");
        recipeService = new RecipeServiceImpl(recipeRepository, restTemplateBuilder, transactionManager);
    }

    @Test
    void getRecipeById_WhenRecipeExists_ReturnsRecipe() {
        // Arrange
        Long recipeId = 1L;
        Recipe expectedRecipe = new Recipe();
        expectedRecipe.setId(recipeId);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(expectedRecipe));

        // Act
        Recipe actualRecipe = recipeService.getRecipeById(recipeId);

        // Assert
        assertNotNull(actualRecipe);
        assertEquals(recipeId, actualRecipe.getId());
        verify(recipeRepository).findById(recipeId);
    }

    @Test
    void getRecipeById_WhenRecipeDoesNotExist_ThrowsException() {
        // Arrange
        Long recipeId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(recipeId));
        verify(recipeRepository).findById(recipeId);
    }

    @SuppressWarnings("null")
    @Test
    void loadRecipesFromExternalApi_Success() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(new Recipe(), new Recipe());
        RecipesResponse response = new RecipesResponse();
        response.setRecipes(recipes);
        
        when(restTemplate.getForObject(eq(EXTERNAL_API_URL), eq(RecipesResponse.class)))
                .thenReturn(response);
        when(recipeRepository.saveAll(any())).thenReturn(recipes);

        // Act
        recipeService.loadRecipesFromExternalApi();

        // Assert
        verify(restTemplate).getForObject(eq(EXTERNAL_API_URL), eq(RecipesResponse.class));
        verify(recipeRepository).saveAll(eq(recipes));
        verify(restTemplate).getForObject(eq(EXTERNAL_API_URL), eq(RecipesResponse.class));
    }
}
