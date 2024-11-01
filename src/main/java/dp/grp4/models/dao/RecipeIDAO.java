package dp.grp4.models.dao;

import dp.grp4.models.entities.Recipe;

import java.util.List;

public interface RecipeIDAO {
    void add(Recipe recipe);
    List<Recipe> getAll();
    Recipe getById(long id);
    void modify(Recipe recipe);
    void delete(long id);
}
