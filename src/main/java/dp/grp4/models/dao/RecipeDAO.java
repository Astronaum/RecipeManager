package dp.grp4.models.dao;

import dp.grp4.exceptions.DBException;
import dp.grp4.models.db.JsonDB;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDAO implements RecipeIDAO{
    private static final RecipeDAO INSTANCE=new RecipeDAO();
    private static final JsonDB DB=JsonDB.getInstance();
    private RecipeDAO(){}
    public static RecipeDAO getInstance(){
        return INSTANCE;
    }
    @SuppressWarnings("DuplicatedCode")
    @Override
    public long add(Recipe recipe) throws DBException {
        Map<Long,Object> collection=DB.getCollection(Recipe.class);
        long nextId=DB.getNextId(Recipe.class);
        recipe.setId(nextId);
        collection.put(recipe.getId(),recipe);
        DB.setCollection(Recipe.class,collection);
        return nextId;
    }

    @Override
    public List<Recipe> getAll() {
        Map<Long,Object> collection=DB.getCollection(Recipe.class);
        return collection.values().stream().map(v->(Recipe)v).toList();
    }

    @Override
    public Recipe getById(long id) {
        Map<Long,Object> collection=DB.getCollection(Recipe.class);
        return (Recipe)collection.get(id);
    }

    @Override
    public void update(Recipe recipe) throws DBException {
        Map<Long,Object> collection=DB.getCollection(Recipe.class);
        if(!collection.containsKey(recipe.getId()))
            throw new DBException("Can not find the item with ID="+recipe.getId());
        collection.put(recipe.getId(),recipe);
        DB.setCollection(Recipe.class,collection);
    }

    @Override
    public void delete(long id) {
        Map<Long,Object> collection=DB.getCollection(Recipe.class);
        collection.remove(id);
        DB.setCollection(Recipe.class,collection);
    }

    public List<Recipe> filter(
            String name,
            Recipe.Category category,
            Recipe.Difficulty difficulty,
            Boolean favourite,
            Integer maxPreparationTime,
            boolean useAndLogic
    ) {
        Map<Long, Object> collection = DB.getCollection(Recipe.class);

        return collection.values().stream()
                .map(v -> (Recipe) v)
                .filter(recipe -> {
                    boolean matchesName = (name == null || recipe.getName().equalsIgnoreCase(name));
                    boolean matchesCategory = (category == null || recipe.getCategory() == category);
                    boolean matchesDifficulty = (difficulty == null || recipe.getDifficulty() == difficulty);
                    boolean matchesFavourite = (favourite == null || recipe.isFavourite() == favourite);
                    boolean matchesPreparationTime = (maxPreparationTime == null || recipe.getPreparationTime() <= maxPreparationTime);

                    if (useAndLogic) {
                        // All conditions must match
                        return matchesName && matchesCategory && matchesDifficulty && matchesFavourite && matchesPreparationTime;
                    } else {
                        // Any condition must match
                        return matchesName || matchesCategory || matchesDifficulty || matchesFavourite || matchesPreparationTime;
                    }
                })
                .toList();
    }

    public Map<String, List<Recipe>> suggestRecipes(List<Ingredient> availableIngredients) {
        List<Recipe> allRecipes = getAll();

        List<Recipe> completeRecipes = new ArrayList<>();
        List<Recipe> incompleteRecipes = new ArrayList<>();
        List<Recipe> notSuggestedRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            List<Recipe.IngredientQuantity> recipeIngredients = recipe.getIngredients();

            // Count how many ingredients in the recipe have sufficient stock
            long matchCount = 0;

            for (Recipe.IngredientQuantity recipeIngredient : recipeIngredients) {
                // Find the matching ingredient in available ingredients
                Ingredient matchingIngredient = findIngredientById(availableIngredients, recipeIngredient.id());

                if (matchingIngredient != null) {
                    // Check if the available stock is sufficient
                    if (isSufficientStock(matchingIngredient, recipeIngredient)) {
                        matchCount++;
                    }
                }
            }

            // Calculate the match percentage
            double matchPercentage = (double) matchCount / recipeIngredients.size() * 100;

            // Categorize the recipe based on the match percentage
            if (matchPercentage == 100) {
                completeRecipes.add(recipe);  // Fully matched recipe
            } else if (matchPercentage >= 50) {
                incompleteRecipes.add(recipe);  // Incomplete but eligible recipe
            } else {
                notSuggestedRecipes.add(recipe);  // Recipe not suggested
            }
        }

        // Optionally, you can return all the recipes or different lists as needed
        // For example, returning a Map or a custom object that holds all three categories
        Map<String, List<Recipe>> categorizedRecipes = new HashMap<>();
        categorizedRecipes.put("Complete", completeRecipes);
        categorizedRecipes.put("Incomplete", incompleteRecipes);
        categorizedRecipes.put("Not Suggested", notSuggestedRecipes);

        // Return the map for further use, or just return a specific list if needed
        return categorizedRecipes;
    }

    // Helper method to find ingredient by ID
    private Ingredient findIngredientById(List<Ingredient> ingredients, long id) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == id) {
                return ingredient;
            }
        }
        return null;  // If ingredient not found
    }

    // Helper method to check if available stock is sufficient
    private boolean isSufficientStock(Ingredient ingredient, Recipe.IngredientQuantity recipeIngredient) {
        // For simplicity, we assume ingredients are in the same unit in both the recipe and available stock
        return ingredient.getStock() >= recipeIngredient.quantity();
    }





//
//    public void suggestRecipes(List<Ingredient> availableIngredients) {
//        List<Recipe> allRecipes = getAll();  // Fetch all recipes
//        for (Recipe recipe : allRecipes) {
//            int totalIngredients = recipe.getIngredientsIds().size();
//            int availableCount = 0;
//
//            // Count available ingredients for the recipe
//            for (Long ingredientId : recipe.getIngredientsIds()) {
//                for (Ingredient ingredient : availableIngredients) {
//                    if (ingredient.getId() == ingredientId && ingredient.getQuantity() > 0) {
//                        availableCount++;
//                        break;
//                    }
//                }
//            }
//
//            double availablePercentage = (double) availableCount / totalIngredients;
//            if (availablePercentage == 1) {
//                // Suggest and mark as complete
//                System.out.println("Recipe: " + recipe.getName() + " - Complete (all ingredients available).");
//            } else if (availablePercentage > 0.5) {
//                // Suggest and mark as incomplete
//                System.out.println("Recipe: " + recipe.getName() + " - Incomplete (more than 50% ingredients available).");
//            } else {
//                // Do not suggest if less than 50% ingredients are available
//                System.out.println("Recipe: " + recipe.getName() + " - Not Suggested (less than 50% ingredients available).");
//            }
//        }
//    }


}
