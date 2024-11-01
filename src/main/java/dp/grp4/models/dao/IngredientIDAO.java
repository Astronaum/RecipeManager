package dp.grp4.models.dao;

import dp.grp4.models.entities.Ingredient;

import java.util.List;

public interface IngredientIDAO {
    void add(Ingredient ingredient);
    List<Ingredient> getAll();
    Ingredient getById(long id);
    void modify(Ingredient ingredient);
    void delete(long id);
}
