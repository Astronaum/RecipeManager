package dp.grp4.views;

import dp.grp4.orders.OrderType;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class IngredientsView extends InteractiveView {
    public static IngredientsView create(ViewsManager viewsManager) throws IOException {
        return (IngredientsView) InteractiveView.getView(viewsManager,"ingredients.fxml");
    }

    public void gotoHome(MouseEvent e) {
        this.getController().fireOrder(OrderType.SHOW_HOME);
    }
}
