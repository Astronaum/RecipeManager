package dp.grp4.views;

import dp.grp4.orders.OrderType;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HomeView extends InteractiveView {
    public static HomeView create(ViewsManager viewsManager) throws IOException {
        return (HomeView) InteractiveView.getView(viewsManager,"home.fxml");
    }

    public void gotoRecipes(MouseEvent e) {
        this.getController().fireOrder(OrderType.SHOW_RECIPES);
    }

    public void gotoIngredients(MouseEvent e) {
        this.getController().fireOrder(OrderType.SHOW_INGREDIENTS);
    }
}
