package dp.grp4;

import dp.grp4.exceptions.ExceptionHandler;
import dp.grp4.helpers.Helper;
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
//            clearDB();
//            exampleAdd();
//            exampleDeleteUpdate();
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

    public static void main(String[] args) {
        launch();
    }

}