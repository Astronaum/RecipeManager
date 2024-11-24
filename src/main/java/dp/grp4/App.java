package dp.grp4;

import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.dao.IngredientIDAO;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.db.JsonDB;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;
import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App extends Application {
    @Override
    public void start(Stage stage){
        ViewsManager viewsManager = ViewsManager.getInstance(stage);
        viewsManager.processOrder(OrderType.SHOW_HOME);
        example1();
        example2();
    }
    private void example1(){
        IngredientIDAO o=IngredientDAO.getInstance();
        Ingredient i=Ingredient.builder()
                .setId(1).setName("Salt").setUnit(Ingredient.Unit.g).setQuantity(120)
                .build();
        Ingredient j=Ingredient.builder()
                .setId(2).setName("oeuf").setUnit(Ingredient.Unit.unit).setQuantity(6)
                .build();
        o.add(i);
        o.add(j);
        System.out.println(o.getAll());
        // o.delete(i.getId());
        //System.out.println(o.getAll());
    }
    private void example2(){
        RecipeDAO o=RecipeDAO.getInstance();
        Recipe i=Recipe.builder()
                .setId(1).setName("Omelette").setCookingTime(12).setIngredientsIds(Arrays.asList(1L,2L)).setCategory(Recipe.Category.MAIN)
                .setInstructionsList(Arrays.asList(
                        "Crack the eggs into a bowl.",
                        "Whisk the eggs with salt.",
                        "Heat a pan with",
                        "Pour the egg mixture into the pan.",
                        "Cook until set, then fold and serve."
                ))
                .build();
        Recipe j=Recipe.builder()
                .setId(2).setName("Tiramisu").setFavourite(true).setCategory(Recipe.Category.DESSERT)
                .build();
        Recipe k=Recipe.builder()
                .setId(3).setName("Tea").setDifficulty(Recipe.Difficulty.EASY).setPreparationTime(30)
                .build();
        o.add(i);
        o.add(j);
        o.add(k);
        System.out.println(o.getAll());

        //Filter category
        Map<String, Object> criteria = Map.of(
                "category", Recipe.Category.MAIN
        );

        List<Recipe> filteredRecipes = o.filter(criteria);
        filteredRecipes.forEach(recipe -> System.out.println(recipe.getName()));

        //filter difficulty
        Map<String, Object> criteria1 = Map.of(
                "difficulty", Recipe.Difficulty.EASY
        );

        List<Recipe> filteredRecipes1 = o.filter(criteria1);
        filteredRecipes1.forEach(recipe -> System.out.println(recipe.getName()));

        //filter preparationtime
        Map<String, Object> criteria2 = Map.of(
                "preparationTime", 30
        );

        List<Recipe> filteredRecipes2 = o.filter(criteria2);
        filteredRecipes2.forEach(recipe -> System.out.println(recipe.getName()));

        //filter name
        Map<String, Object> criteria3 = Map.of(
                "name", "Tiramisu"
        );

        List<Recipe> filteredRecipes3 = o.filter(criteria3);
        filteredRecipes3.forEach(recipe -> System.out.println(recipe.getName()));


        //o.delete(i.getId());
        //System.out.println(o.getAll());
    }
    public static void main(String[] args) {
        launch();


    }

}