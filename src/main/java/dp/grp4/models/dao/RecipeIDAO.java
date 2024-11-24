package dp.grp4.models.dao;

import dp.grp4.models.entities.Recipe;

import java.util.List;
import java.util.Map;

public interface RecipeIDAO {
    void add(Recipe recipe);
    List<Recipe> getAll();
    Recipe getById(long id);
    void modify(Recipe recipe);
    void delete(long id);
    public List<Recipe> filter(Map<String, Object> criteria, boolean OrAndChoice);
}
