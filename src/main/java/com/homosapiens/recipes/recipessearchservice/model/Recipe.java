package com.homosapiens.recipes.recipessearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Indexed(index = "Recipe")
@Table(name = "recipes", indexes = {
    @Index(name = "idx_recipe_name", columnList = "name"),
    @Index(name = "idx_recipe_cuisine", columnList = "cuisine")
})
public class Recipe {
    @Id
    @GenericField
    private Long id;

    @FullTextField
    private String name;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_instructions", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "instruction")
    private List<String> instructions;

    private Integer prepTimeMinutes;
    private Integer cookTimeMinutes;
    private Integer servings;
    private String difficulty;

    @FullTextField
    private String cuisine;

    private Integer caloriesPerServing;

    @ElementCollection
    @CollectionTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "tag")
    private List<String> tags;

    private Long userId;
    private String image;
    private Double rating;
    private Integer reviewCount;

    @ElementCollection
    @CollectionTable(name = "recipe_meal_types", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "meal_type")
    private List<String> mealType;
}
