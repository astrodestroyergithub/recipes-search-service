package com.homosapiens.recipes.recipessearchservice.service.impl;

import com.homosapiens.recipes.recipessearchservice.exception.ExternalApiException;
import com.homosapiens.recipes.recipessearchservice.exception.RecipeNotFoundException;
import com.homosapiens.recipes.recipessearchservice.model.Recipe;
import com.homosapiens.recipes.recipessearchservice.model.RecipesResponse;
import com.homosapiens.recipes.recipessearchservice.repository.RecipeRepository;
import com.homosapiens.recipes.recipessearchservice.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.NonNull;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Service
@Transactional
public class RecipeServiceImpl implements RecipeService, InitializingBean {
    private static final String EXTERNAL_API_URL = "https://dummyjson.com/recipes";
    
    private final RecipeRepository recipeRepository;
    private final RestTemplate restTemplate;
    private final org.springframework.transaction.PlatformTransactionManager transactionManager;
    
    @PersistenceContext
    private EntityManager entityManager;

    public RecipeServiceImpl(RecipeRepository recipeRepository, 
                           RestTemplateBuilder restTemplateBuilder,
                           @NonNull org.springframework.transaction.PlatformTransactionManager transactionManager) {
        this.recipeRepository = recipeRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.transactionManager = transactionManager;
    }

    @Override
    public Recipe getRecipeById(@NonNull Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
    }

    @Override
    public List<Recipe> searchRecipes(@NonNull String searchTerm) {
        SearchSession searchSession = Search.session(entityManager);
        
        return searchSession.search(Recipe.class)
            .where(f -> f.wildcard()
                .fields("name", "cuisine")
                .matching("*" + searchTerm + "*"))
            .fetchAllHits();
    }

    @Override
    public void afterPropertiesSet() {
        log.info("RecipeServiceImpl bean initialization starting...");
        try {
            org.springframework.transaction.support.TransactionTemplate transactionTemplate = 
                new org.springframework.transaction.support.TransactionTemplate(transactionManager);

            transactionTemplate.execute(status -> {
                loadRecipesFromExternalApi();
                try {
                    rebuildSearchIndex();
                } catch (InterruptedException e) {
                    log.error("Search index rebuild was interrupted", e);
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to rebuild search index", e);
                }
                return null;
            });
            log.info("RecipeServiceImpl bean initialization completed");
        } catch (Exception e) {
            log.error("Failed to initialize recipes", e);
            throw new ExternalApiException("Failed to initialize recipes", e);
        }
    }

    @Override
    public void loadRecipesFromExternalApi() {
        log.info("Loading recipes from external API...");
        try {
            RecipesResponse response = restTemplate.getForObject(EXTERNAL_API_URL, RecipesResponse.class);
            List<Recipe> recipes = response != null ? response.getRecipes() : null;
            if (recipes != null && !recipes.isEmpty()) {
                recipeRepository.saveAll(recipes);
                log.info("Successfully saved {} recipes", recipes.size());
            } else {
                log.warn("No recipes found in the API response");
            }
        } catch (Exception e) {
            log.error("Error loading recipes from external API", e);
            throw new ExternalApiException("Failed to load recipes from external API", e);
        }
    }

    @Transactional(readOnly = true)
    protected void rebuildSearchIndex() throws InterruptedException {
        log.info("Starting search index rebuild...");
        try {
            SearchSession searchSession = Search.session(entityManager);
            MassIndexer indexer = searchSession.massIndexer(Recipe.class)
                    .threadsToLoadObjects(4)
                    .batchSizeToLoadObjects(50)
                    .idFetchSize(150)
                    .transactionTimeout(300); // 5 minutes
            indexer.startAndWait();
            log.info("Search index rebuild completed successfully");
        } catch (Exception e) {
            log.error("Error rebuilding search index", e);
            throw e;
        }
    }
}
