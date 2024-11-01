package dp.grp4.models.dao;

import dp.grp4.models.db.JsonDB;
import dp.grp4.models.entities.Ingredient;

import java.util.List;
import java.util.Map;

public class IngredientDAO implements IngredientIDAO{
    private final String collectionName="ingredients";
    private static final IngredientDAO INSTANCE=new IngredientDAO();
    private static final JsonDB DB=JsonDB.getInstance();
    private IngredientDAO(){}
    public static IngredientDAO getInstance(){
        return INSTANCE;
    }

    @Override
    public void add(Ingredient ingredient) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        if(collection.containsKey(ingredient.getId()))
            throw new RuntimeException("Can not add two items with the same ID");
        collection.put(ingredient.getId(),ingredient);
        DB.setCollection(collectionName,collection);
    }

    @Override
    public List<Ingredient> getAll() {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        return collection.values().stream().map(v->(Ingredient)v).toList();
    }

    @Override
    public Ingredient getById(long id) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        return (Ingredient)collection.get(id);
    }

    @Override
    public void modify(Ingredient ingredient) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        if(!collection.containsKey(ingredient.getId()))
            throw new RuntimeException("Can not find the item with ID="+ingredient.getId());
        collection.put(ingredient.getId(),ingredient);
        DB.setCollection(collectionName,collection);
    }

    @Override
    public void delete(long id) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        collection.remove(id);
        DB.setCollection(collectionName,collection);
    }
}
