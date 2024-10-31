package dp.grp4.views;

import dp.grp4.orders.OrderType;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RecipesView extends InteractiveView {
    public static RecipesView create(ViewsManager viewsManager) throws IOException {
        return (RecipesView) InteractiveView.getView(viewsManager,"recipes.fxml");
    }
    public void gotoHome(MouseEvent e) {
        this.getController().fireOrder(OrderType.SHOW_HOME);
    }
}
