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
import java.util.Arrays;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        ExceptionHandler.context(()->{
            ViewsManager.init(stage);
            ViewsManager.getInstance().launch();
            clearDB();
            exampleAdd();
            exampleDeleteUpdate();
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
                .setStock(30).setUnit(Ingredient.Unit.unit).setName("Eggs").setExpirationDate(LocalDate.now())
                .build();
        Ingredient i3=Ingredient.builder()
                .setStock(15).setUnit(Ingredient.Unit.unit).setName("Oranges").setExpirationDate(LocalDate.now())
                .build();

        long idI1=ingredientDAO.add(i1);
        long idI2=ingredientDAO.add(i2);
        long idI3=ingredientDAO.add(i3);

        Recipe r=Recipe.builder()
                .setName("Omelette").setCookingTime(12)
                .setIngredients(Arrays.asList(
                        new Recipe.IngredientQuantity(idI1,10),
                        new Recipe.IngredientQuantity(idI2,5),
                        new Recipe.IngredientQuantity(idI3,3)
                ))
                .setCategory(Recipe.Category.MAIN)
                .setInstructionsList(Arrays.asList(
                        "Crack the eggs into a bowl.",
                        "Whisk the eggs with salt.",
                        "Heat a pan with",
                        "Pour the egg mixture into the pan.",
                        "Cook until set, then fold and serve."
                ))
                .build();
        recipeDAO.add(r);

    }

//    private void example1() throws DBException {
//        IngredientIDAO o=IngredientDAO.getInstance();
//        Ingredient i=Ingredient.builder()
//                .setId(1).setName("Salt").setUnit(Ingredient.Unit.g).setQuantity(120)
//                .build();
//        Ingredient j=Ingredient.builder()
//                .setId(2).setName("pepper").setUnit(Ingredient.Unit.g).setQuantity(0)
//                .build();
//        Ingredient k=Ingredient.builder()
//                .setId(3).setName("egg").setUnit(Ingredient.Unit.unit).setQuantity(6)
//                .build();
//        Ingredient l=Ingredient.builder()
//                .setId(4).setName("milk").setUnit(Ingredient.Unit.L).setQuantity(0)
//                .build();
//        Ingredient m=Ingredient.builder()
//                .setId(5).setName("Tea").setUnit(Ingredient.Unit.g).setQuantity(200)
//                .build();
//        o.add(i);
//        o.add(j);
//        o.add(k);
//        o.add(l);
//        o.add(m);
//        System.out.println(o.getAll());
//        // o.delete(i.getId());
//        //System.out.println(o.getAll());
//    }
//    private void example2(){
//        RecipeDAO o=RecipeDAO.getInstance();
//        Recipe i=Recipe.builder()
//                .setId(1).setName("Omelette").setCookingTime(12).setIngredientsIds(Arrays.asList(1L,2L,3L)).setCategory(Recipe.Category.MAIN)
//                .setInstructionsList(Arrays.asList(
//                        "Crack the eggs into a bowl.",
//                        "Whisk the eggs with salt.",
//                        "Heat a pan with",
//                        "Pour the egg mixture into the pan.",
//                        "Cook until set, then fold and serve."
//                ))
//                .build();
//        Recipe l=Recipe.builder()
//                .setId(4).setName("Oeuf_bouillie").setCookingTime(12).setIngredientsIds(Arrays.asList(3L,2L,4L)).setCategory(Recipe.Category.MAIN)
//                .build();
//        Recipe j=Recipe.builder()
//                .setId(2).setName("Tiramisu").setFavourite(true).setCategory(Recipe.Category.DESSERT)
//                .build();
//        Recipe k=Recipe.builder()
//                .setId(3).setName("Tea").setIngredientsIds(List.of(5L)).setDifficulty(Recipe.Difficulty.EASY).setPreparationTime(30)
//                .build();
//        o.add(i);
//        //o.add(j);
//        o.add(k);
//        o.add(l);
//        System.out.println(o.getAll());
//
//        //Filter category
//        Map<String, Object> criteria = Map.of(
//                "category", Recipe.Category.MAIN
//        );
//
//        //List<Recipe> filteredRecipes = o.filter(criteria, false);
//        //filteredRecipes.forEach(recipe -> System.out.println(recipe.getName()));
//
//        //filter difficulty
//        Map<String, Object> criteria1 = Map.of(
//                "difficulty", Recipe.Difficulty.EASY
//        );
//
//        //List<Recipe> filteredRecipes1 = o.filter(criteria1, false);
//        //filteredRecipes1.forEach(recipe -> System.out.println(recipe.getName()));
//
//        //filter preparationtime
//        Map<String, Object> criteria2 = Map.of(
//                "preparationTime", 30
//        );
//
//        //List<Recipe> filteredRecipes2 = o.filter(criteria2, false);
//        //filteredRecipes2.forEach(recipe -> System.out.println(recipe.getName()));
//
//        //filter name
//        Map<String, Object> criteria3 = Map.of(
//                "name", "Oeuf_bouillie",
//                "preparationTime", 30
//        );
//
//        List<Recipe> filteredRecipes3 = o.filter(criteria3, false);
//        filteredRecipes3.forEach(recipe -> System.out.println(recipe.getName()));
//
//
//        //Suggestion
//        IngredientDAO ingredientDAO = IngredientDAO.getInstance();
//        List<Ingredient> availableIngredients = ingredientDAO.getAll(); // This should be your list of available ingredients.
//
//        RecipeDAO recipeDAO = RecipeDAO.getInstance();
//        recipeDAO.suggestRecipes(availableIngredients);
//
//        //o.delete(i.getId());
//        //System.out.println(o.getAll());
//    }
    public static void main(String[] args) {
        launch();
    }

}