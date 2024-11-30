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
            throw new DBException("Can not find the item with ID="+recipe.getId(),5);
        collection.put(recipe.getId(),recipe);
        DB.setCollection(Recipe.class,collection);
    }

    @Override
    public void delete(long id) {
        Map<Long,Object> collection=DB.getCollection(Recipe.class);
        collection.remove(id);
        DB.setCollection(Recipe.class,collection);
    }
    @Override
    public List<Recipe> filter(
            String name,
            Recipe.Category category,
            Recipe.Difficulty difficulty,
            Recipe.Criteria criteria,
            Integer maxPreparationTime,
            boolean useAndLogic
    ) {
        Map<Long, Object> collection = DB.getCollection(Recipe.class);

        return collection.values().stream()
                .map(v -> (Recipe) v)
                .filter(recipe -> {
                    boolean matchesName = (name == null || recipe.getName().toLowerCase().contains(name.toLowerCase()));
                    boolean matchesCategory = (category == null || recipe.getCategory() == category);
                    boolean matchesDifficulty = (difficulty == null || recipe.getDifficulty() == difficulty);
                    boolean matchesCriteria = (criteria == null || recipe.getCriteria() == criteria);
                    boolean matchesPreparationTime = (maxPreparationTime == null || recipe.getPreparationTime() <= maxPreparationTime);

                    if (useAndLogic) {
                        // AND logic: All conditions must be true
                        return matchesName && matchesCategory && matchesDifficulty && matchesCriteria && matchesPreparationTime;
                    } else {
                        // OR logic: At least one condition must be true
                        // If all conditions are null, return all recipes
                        return (name != null && matchesName)
                                || (category != null && matchesCategory)
                                || (difficulty != null && matchesDifficulty)
                                || (criteria != null && matchesCriteria)
                                || (maxPreparationTime != null && matchesPreparationTime)
                                || (name == null && category == null && difficulty == null && criteria == null && maxPreparationTime == null);
                    }
                })
                .toList();
    }


    @Override
    public Map<String, List<Recipe>> suggestRecipes(List<Ingredient> availableIngredients) {
        List<Recipe> allRecipes = getAll();

        List<Recipe> completeRecipes = new ArrayList<>();
        List<Recipe> incompleteRecipes = new ArrayList<>();
        List<Recipe> notSuggestedRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            List<Recipe.IngredientQuantity> recipeIngredients = recipe.getIngredients();

            //Comptez combien d'ingrédients dans la recette ont un stock suffisant.
            long matchCount = 0;

            for (Recipe.IngredientQuantity recipeIngredient : recipeIngredients) {
                Ingredient matchingIngredient = findIngredientById(availableIngredients, recipeIngredient.id());

                if (matchingIngredient != null) {
                    if (isSufficientStock(matchingIngredient, recipeIngredient)) {
                        matchCount++;
                    }
                }
            }

            // Calcul du matching pourcentage
            double matchPercentage = (double) matchCount / recipeIngredients.size() * 100;

            if (matchPercentage == 100) {
                completeRecipes.add(recipe);  // Les complètes
            } else if (matchPercentage >= 50) {
                incompleteRecipes.add(recipe);  // Les incomplètes
            } else {
                notSuggestedRecipes.add(recipe);  // Les non suggérées
            }
        }

        Map<String, List<Recipe>> categorizedRecipes = new HashMap<>();
        categorizedRecipes.put("Complete", completeRecipes);
        categorizedRecipes.put("Incomplete", incompleteRecipes);
        categorizedRecipes.put("Not Suggested", notSuggestedRecipes);

        return categorizedRecipes;
    }

    private Ingredient findIngredientById(List<Ingredient> ingredients, long id) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == id) {
                return ingredient;
            }
        }
        return null;
    }

    private boolean isSufficientStock(Ingredient ingredient, Recipe.IngredientQuantity recipeIngredient) {
        return ingredient.getStock() >= recipeIngredient.quantity();
    }
    @Override
    public Map<Ingredient, Integer> getMissingIngredients(Recipe recipe, List<Ingredient> availableIngredients) {
        Map<Ingredient, Integer> missingIngredients = new HashMap<>();

        for (Recipe.IngredientQuantity required : recipe.getIngredients()) {
            Ingredient matchingIngredient = availableIngredients.stream()
                    .filter(ingredient -> ingredient.getId() == required.getId())
                    .findFirst()
                    .orElse(null);

            if (matchingIngredient == null || matchingIngredient.getStock() < required.getQuantity()) {
                int missingQuantity = matchingIngredient == null
                        ? required.getQuantity()
                        : required.getQuantity() - matchingIngredient.getStock();
                missingIngredients.put(matchingIngredient, missingQuantity);
            }
        }

        return missingIngredients;
    }

}
