package dp.grp4.models.dao;

import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;

import java.util.List;

public interface RecipeDAO {
    void add(Recipe recipe);
    List<Recipe> getAll();
    Ingredient getById(int id);
    void modify(Recipe recipe);
    void delete(int id);
}
