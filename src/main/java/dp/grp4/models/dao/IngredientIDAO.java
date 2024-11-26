package dp.grp4.models.dao;

import dp.grp4.exceptions.DBException;
import dp.grp4.models.entities.Ingredient;

import java.util.List;

public interface IngredientIDAO {
    long add(Ingredient ingredient) throws DBException;
    List<Ingredient> getAll();
    Ingredient getById(long id);
    void update(Ingredient ingredient) throws DBException;
    void delete(long id);
}
