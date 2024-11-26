package dp.grp4;

import dp.grp4.exceptions.ExceptionHandler;
import dp.grp4.models.dao.RecipeIDAO;
import dp.grp4.exceptions.DBException;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.dao.IngredientIDAO;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;
import dp.grp4.views.ViewsManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        ExceptionHandler.context(()->{
            ViewsManager.init(stage);
            ViewsManager.getInstance().launch();
            clearDB();
            exampleAdd();
            //exampleDeleteUpdate();
        });
    }
    private void clearDB(){
        IngredientIDAO ingredientDAO=IngredientDAO.getInstance();
        RecipeIDAO recipeDAO=RecipeDAO.getInstance();
        ingredientDAO.getAll().forEach(i-> ingredientDAO.delete(i.getId()));
        recipeDAO.getAll().forEach(i-> recipeDAO.delete(i.getId()));
    }
    private void exampleDeleteUpdate() throws DBException {
        IngredientIDAO ingredientDAO=IngredientDAO.getInstance();
        RecipeIDAO recipeDAO=RecipeDAO.getInstance();
        ingredientDAO.delete(1);
        ingredientDAO.update(Ingredient.builder().setId(2).setStock(40).build());
    }
    private void exampleAdd() throws DBException {
        IngredientIDAO ingredientDAO=IngredientDAO.getInstance();
        RecipeIDAO recipeDAO=RecipeDAO.getInstance();

        Ingredient i1=Ingredient.builder()
                .setStock(10).setUnit(Ingredient.Unit.L).setName("Lait").setExpirationDate(LocalDate.now())
                .build();
        Ingredient i2=Ingredient.builder()
                .setStock(5).setUnit(Ingredient.Unit.unit).setName("Eggs").setExpirationDate(LocalDate.now())
                .build();
        Ingredient i3=Ingredient.builder()
                .setStock(1).setUnit(Ingredient.Unit.unit).setName("Oranges").setExpirationDate(LocalDate.now())
                .build();

        Ingredient i4=Ingredient.builder()
                .setStock(0).setUnit(Ingredient.Unit.unit).setName("coffee").setExpirationDate(LocalDate.now())
                .build();

        Ingredient i5=Ingredient.builder()
                .setStock(1).setUnit(Ingredient.Unit.unit).setName("Sugar").setExpirationDate(LocalDate.now())
                .build();

        long idI1=ingredientDAO.add(i1);
        long idI2=ingredientDAO.add(i2);
        long idI3=ingredientDAO.add(i3);
        long idI4=ingredientDAO.add(i4);
        long idI5=ingredientDAO.add(i5);


        Recipe r=Recipe.builder()
                .setName("Omelette").setCookingTime(15)
                .setIngredients(Arrays.asList(
                        new Recipe.IngredientQuantity(idI1,10),
                        new Recipe.IngredientQuantity(idI2,5),
                        new Recipe.IngredientQuantity(idI3,1)
                ))
                .setCategory(Recipe.Category.MAIN)
                .setInstructionsList(Arrays.asList(
                        "Crack the eggs into a bowl.",
                        "Whisk the eggs with salt.",
                        "Heat a pan with",
                        "Pour the egg mixture into the pan.",
                        "Cook until set, then fold and serve."
                ))
                .setPreparationTime(30)
                .setDifficulty(Recipe.Difficulty.EASY)
                .build();

        Recipe r_ =Recipe.builder()
                .setName("Tiramisu").setCookingTime(12)
                .setIngredients(Arrays.asList(
                        new Recipe.IngredientQuantity(idI4,1),
                        new Recipe.IngredientQuantity(idI2,3),
                        new Recipe.IngredientQuantity(idI3,2)
                ))
                .setCategory(Recipe.Category.DESSERT)
                .setInstructionsList(Arrays.asList(
                        "Crack the eggs into a bowl.",
                        "Whisk the eggs.",
                        "Add coffee"
                ))
                .setFavourite(true)
                .setPreparationTime(10)
                .build();

        Recipe r__ =Recipe.builder()
                .setName("Hot coffee").setCookingTime(5)
                .setIngredients(Arrays.asList(
                        new Recipe.IngredientQuantity(idI4,1),
                        new Recipe.IngredientQuantity(idI5,1)
                ))
                .setCategory(Recipe.Category.DESSERT)
                .setFavourite(true)
                .setPreparationTime(5)
                .build();

        recipeDAO.add(r__);
        recipeDAO.add(r_);
        recipeDAO.add(r);

        System.out.println("Testing filters ------------------");
        List<Recipe> andRecipes = recipeDAO.filter(
                null, Recipe.Category.MAIN, Recipe.Difficulty.EASY, null, null, true
        );

        List<Recipe> orRecipes = recipeDAO.filter(
                null, Recipe.Category.MAIN, null, true, 10, false
        );

        System.out.println("Recipes with AND logic:");
        andRecipes.forEach(recipe -> System.out.println(recipe.getName()));

        System.out.println("Recipes with AND logic:");
        orRecipes.forEach(recipe -> System.out.println(recipe.getName()));

        System.out.println("Testing suggestions ------------------");
        Map<String, List<Recipe>> categorizedRecipes = recipeDAO.suggestRecipes(IngredientDAO.getInstance().getAll());

        List<Recipe> complete = categorizedRecipes.get("Complete");
        List<Recipe> incomplete = categorizedRecipes.get("Incomplete");
        List<Recipe> notSuggested = categorizedRecipes.get("Not Suggested");


        System.out.println("Complete Recipes: " + complete.size());
        complete.forEach(recipe -> System.out.println(recipe.getName()));
        System.out.println("Incomplete Recipes: " + incomplete.size());
        incomplete.forEach(recipe -> System.out.println(recipe.getName()));
        System.out.println("Not Suggested Recipes: " + notSuggested.size());
        notSuggested.forEach(recipe -> System.out.println(recipe.getName()));
    }


    public static void main(String[] args) {
        launch();
    }

}