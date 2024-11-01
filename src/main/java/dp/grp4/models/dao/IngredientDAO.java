package dp.grp4.models.dao;

import dp.grp4.models.entities.Ingredient;

import java.util.List;

public interface IngredientDAO {
    void add(Ingredient ingredient);
    List<Ingredient> getAll();
    Ingredient getById(int id);
    void modify(Ingredient ingredient);
    void delete(int id);
}
