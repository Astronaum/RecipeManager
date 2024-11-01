package dp.grp4.views;

import dp.grp4.controllers.HomeController;
import dp.grp4.controllers.RecipesController;
import dp.grp4.orders.OrderType;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RecipesView extends InteractiveView {
    public static RecipesView create(ViewsManager viewsManager) throws IOException {
        RecipesController recipesController=RecipesController.create(viewsManager);
        RecipesView recipesView= (RecipesView) InteractiveView.getView(viewsManager,"recipes.fxml");
        recipesView.setController(recipesController);
        return recipesView;
    }
    public RecipesController getController(){
        return (RecipesController) super.getController();
    }
    public void gotoHome(MouseEvent e) {
        this.getController().gotoHome();
    }
}
