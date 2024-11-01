package dp.grp4;

import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.dao.IngredientIDAO;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;
import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;
import javafx.application.Application;
import javafx.stage.Stage;

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
                .setId(2).setName("AAA").setUnit(Ingredient.Unit.g).setQuantity(120)
                .build();
        o.add(i);
        o.add(j);
        System.out.println(o.getAll());
        o.delete(i.getId());
        System.out.println(o.getAll());
    }
    private void example2(){
        RecipeDAO o=RecipeDAO.getInstance();
        Recipe i=Recipe.builder()
                .setId(1).setName("Salt").setCookingTime(12)
                .build();
        Recipe j=Recipe.builder()
                .setId(2).setName("AAA").setFavourite(true)
                .build();
        o.add(i);
        o.add(j);
        System.out.println(o.getAll());
        o.delete(i.getId());
        System.out.println(o.getAll());
    }
    public static void main(String[] args) {
        launch();
    }

}