package dp.grp4.models.dao;

import dp.grp4.exceptions.DBException;
import dp.grp4.models.entities.Recipe;

import java.util.List;

public interface RecipeIDAO {
    long add(Recipe recipe) throws DBException;
    List<Recipe> getAll();
    Recipe getById(long id);
    void update(Recipe recipe) throws DBException;
    void delete(long id);
    //public List<Recipe> filter(Map<String, Object> criteria, boolean OrAndChoice);
}
