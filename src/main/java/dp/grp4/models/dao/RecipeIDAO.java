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
    public List<Recipe> filter(
            String name,
            Recipe.Category category,
            Recipe.Difficulty difficulty,
            Boolean favourite,
            Integer maxPreparationTime,
            boolean useAndLogic
    );
    public Map<String, List<Recipe>>suggestRecipes(List<Ingredient> availableIngredients);
}
