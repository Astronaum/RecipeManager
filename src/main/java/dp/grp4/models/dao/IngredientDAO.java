package dp.grp4.models.dao;

import dp.grp4.exceptions.DBException;
import dp.grp4.models.db.JsonDB;
import dp.grp4.models.entities.Ingredient;

import java.util.List;
import java.util.Map;

public class IngredientDAO implements IngredientIDAO{
    private static final IngredientDAO INSTANCE=new IngredientDAO();
    private static final JsonDB DB=JsonDB.getInstance();
    private IngredientDAO(){}
    public static IngredientDAO getInstance(){
        return INSTANCE;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public long add(Ingredient ingredient) throws DBException {
        Map<Long,Object> collection=DB.getCollection(Ingredient.class);
        long nextId=DB.getNextId(Ingredient.class);
        ingredient.setId(nextId);
        collection.put(ingredient.getId(),ingredient);
        DB.setCollection(Ingredient.class,collection);
        return nextId;
    }

    @Override
    public List<Ingredient> getAll() {
        Map<Long,Object> collection=DB.getCollection(Ingredient.class);
        return collection.values().stream().map(v->(Ingredient)v).toList();
    }

    @Override
    public Ingredient getById(long id) {
        Map<Long,Object> collection=DB.getCollection(Ingredient.class);
        return (Ingredient)collection.get(id);
    }

    @Override
    public void update(Ingredient ingredient) throws DBException {
        Map<Long,Object> collection=DB.getCollection(Ingredient.class);
        if(!collection.containsKey(ingredient.getId()))
            throw new DBException("Can not find the item with ID="+ingredient.getId());
        collection.put(ingredient.getId(),ingredient);
        DB.setCollection(Ingredient.class,collection);
    }

    @Override
    public void delete(long id) {
        Map<Long,Object> collection=DB.getCollection(Ingredient.class);
        collection.remove(id);
        DB.setCollection(Ingredient.class,collection);
    }
}
