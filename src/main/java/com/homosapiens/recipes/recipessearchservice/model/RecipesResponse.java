package com.homosapiens.recipes.recipessearchservice.model;

import lombok.Data;
import java.util.List;

@Data
public class RecipesResponse {
    private List<Recipe> recipes;
}
