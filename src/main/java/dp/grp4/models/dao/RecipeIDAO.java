package dp.grp4.models.dao;

import dp.grp4.exceptions.DBException;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;

import java.util.List;
import java.util.Map;

public interface RecipeIDAO {
    long add(Recipe recipe) throws DBException;
    List<Recipe> getAll();
    Recipe getById(long id);
    void update(Recipe recipe) throws DBException;
    void delete(long id);
    List<Recipe> filter(
            String name,
            Recipe.Category category,
            Recipe.Difficulty difficulty,
            Recipe.Criteria criteria,
            Integer maxPreparationTime,
            boolean useAndLogic
    );
    Map<String, List<Recipe>>suggestRecipes(List<Ingredient> availableIngredients);

    Map<Ingredient, Integer> getMissingIngredients(Recipe recipe, List<Ingredient> availableIngredients);
}
