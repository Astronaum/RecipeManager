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

    @Override
    public List<Recipe> filter(Map<String, Object> criteria) {
        Map<Long, Object> collection = DB.getCollection(collectionName);

        return collection.values().stream()
                .map(obj -> (Recipe) obj) // Cast objects to Recipe
                .filter(recipe -> {
                    boolean matches = false; // Start with OR logic (no match initially)
                    for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        // Apply filtering conditions for each key
                        switch (key) {
                            case "category":
                                if (value instanceof Recipe.Category)
                                    matches = matches || recipe.getCategory() == value;
                                break;
                            case "difficulty":
                                if (value instanceof Recipe.Difficulty)
                                    matches = matches || recipe.getDifficulty() == value;
                                break;
                            case "favourite":
                                if (value instanceof Boolean)
                                    matches = matches || recipe.isFavourite() == (Boolean) value;
                                break;
                            case "preparationTime":
                                if (value instanceof Integer)
                                    matches = matches || recipe.getPreparationTime() == (Integer) value;
                                break;
                            case "name":
                                if (value instanceof String)
                                    matches = matches || recipe.getName().toLowerCase().contains(((String) value).toLowerCase());
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported filter key: " + key);
                        }

                        // Stop checking further if a condition is satisfied
                        if (matches) break;
                    }
                    return matches;
                })
                .toList();
    }


}
