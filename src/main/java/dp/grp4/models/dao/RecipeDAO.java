package dp.grp4.models.dao;

import dp.grp4.models.db.JsonDB;
import dp.grp4.models.entities.Recipe;

import java.util.List;
import java.util.Map;

public class RecipeDAO implements RecipeIDAO{
    private final String collectionName="recipes";
    private static final RecipeDAO INSTANCE=new RecipeDAO();
    private static final JsonDB DB=JsonDB.getInstance();
    private RecipeDAO(){}
    public static RecipeDAO getInstance(){
        return INSTANCE;
    }

    @Override
    public void add(Recipe recipe) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        if(collection.containsKey(recipe.getId()))
            throw new RuntimeException("Can not add two items with the same ID");
        collection.put(recipe.getId(),recipe);
        DB.setCollection(collectionName,collection);
    }

    @Override
    public List<Recipe> getAll() {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        return collection.values().stream().map(v->(Recipe)v).toList();
    }

    @Override
    public Recipe getById(long id) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        return (Recipe)collection.get(id);
    }

    @Override
    public void modify(Recipe recipe) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        if(!collection.containsKey(recipe.getId()))
            throw new RuntimeException("Can not find the item with ID="+recipe.getId());
        collection.put(recipe.getId(),recipe);
        DB.setCollection(collectionName,collection);
    }

    @Override
    public void delete(long id) {
        Map<Long,Object> collection=DB.getCollection(collectionName);
        collection.remove(id);
        DB.setCollection(collectionName,collection);
    }
}
