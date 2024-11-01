package dp.grp4.views;

import dp.grp4.controllers.IngredientsController;
import dp.grp4.orders.OrderType;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class IngredientsView extends InteractiveView {
    public static IngredientsView create(ViewsManager viewsManager) throws IOException {
        IngredientsController ingredientsController=IngredientsController.create(viewsManager);
        IngredientsView ingredientsView=(IngredientsView) InteractiveView.getView(viewsManager,"ingredients.fxml");
        ingredientsView.setController(ingredientsController);
        return ingredientsView;
    }
    public IngredientsController getController(){
        return (IngredientsController) super.getController();
    }
    public void gotoHome(MouseEvent e) {
        this.getController().fireOrder(OrderType.SHOW_HOME);
    }
}
