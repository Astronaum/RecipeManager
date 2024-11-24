package dp.grp4.models.dao;

import dp.grp4.models.db.JsonDB;
import dp.grp4.models.entities.Ingredient;
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

    public List<Recipe> filter(Map<String, Object> criteria, boolean OrAndChoice) {
        Map<Long, Object> collection = DB.getCollection(collectionName);

        return collection.values().stream()
                .map(obj -> (Recipe) obj) // Cast objects to Recipe
                .filter(recipe -> {
                    boolean matches = OrAndChoice; // Initialize based on filter type (AND or OR)

                    for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        // Apply filtering conditions
                        switch (key) {
                            case "category":
                                if (value instanceof Recipe.Category)
                                    matches = OrAndChoice ? matches && recipe.getCategory() == value
                                            : matches || recipe.getCategory() == value;
                                break;
                            case "difficulty":
                                if (value instanceof Recipe.Difficulty)
                                    matches = OrAndChoice ? matches && recipe.getDifficulty() == value
                                            : matches || recipe.getDifficulty() == value;
                                break;
                            case "favourite":
                                if (value instanceof Boolean)
                                    matches = OrAndChoice ? matches && recipe.isFavourite() == (Boolean) value
                                            : matches || recipe.isFavourite() == (Boolean) value;
                                break;
                            case "preparationTime":
                                if (value instanceof Integer)
                                    matches = OrAndChoice ? matches && recipe.getPreparationTime() == (Integer) value
                                            : matches || recipe.getPreparationTime() == (Integer) value;
                                break;
                            case "name":
                                if (value instanceof String)
                                    matches = OrAndChoice ? matches && recipe.getName().toLowerCase().contains(((String) value).toLowerCase())
                                            : matches || recipe.getName().toLowerCase().contains(((String) value).toLowerCase());
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported filter key: " + key);
                        }

                        // Break early based on filter type
                        if (OrAndChoice && !matches) break; // Fail fast for AND
                        if (!OrAndChoice && matches) break; // Success fast for OR
                    }
                    return matches;
                })
                .toList();
    }

    public void suggestRecipes(List<Ingredient> availableIngredients) {
        List<Recipe> allRecipes = getAll();  // Fetch all recipes
        for (Recipe recipe : allRecipes) {
            int totalIngredients = recipe.getIngredientsIds().size();
            int availableCount = 0;

            // Count available ingredients for the recipe
            for (Long ingredientId : recipe.getIngredientsIds()) {
                for (Ingredient ingredient : availableIngredients) {
                    if (ingredient.getId() == ingredientId && ingredient.getQuantity() > 0) {
                        availableCount++;
                        break;
                    }
                }
            }

            double availablePercentage = (double) availableCount / totalIngredients;
            if (availablePercentage == 1) {
                // Suggest and mark as complete
                System.out.println("Recipe: " + recipe.getName() + " - Complete (all ingredients available).");
            } else if (availablePercentage > 0.5) {
                // Suggest and mark as incomplete
                System.out.println("Recipe: " + recipe.getName() + " - Incomplete (more than 50% ingredients available).");
            } else {
                // Do not suggest if less than 50% ingredients are available
                System.out.println("Recipe: " + recipe.getName() + " - Not Suggested (less than 50% ingredients available).");
            }
        }
    }


}
