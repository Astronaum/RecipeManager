package dp.grp4.views;

import dp.grp4.controllers.HomeController;
import dp.grp4.helpers.Helper;
import dp.grp4.orders.OrderType;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HomeView extends InteractiveView {
    public static HomeView create(ViewsManager viewsManager) throws IOException {
        HomeController homeController=HomeController.create(viewsManager);
        HomeView homeView=(HomeView) InteractiveView.getView(viewsManager,"home.fxml");
        homeView.setController(homeController);
        return homeView;
    }
    public HomeController getController(){
        return (HomeController) super.getController();
    }
    public void gotoRecipes(MouseEvent e) {
        this.getController().gotoRecipes();
    }
    public void gotoIngredients(MouseEvent e) {
        this.getController().gotoIngredients();
    }

    public void openModal(MouseEvent e) {
        ConfirmModalView modalView = Helper.getViewInstance(ConfirmModalView.class);
        modalView.setModalText("Ensure that this.data.get(collectionName) contains correctly typed objects (e.g., Recipe instances). Add type checks during the deserialization process or explicitly cast objects when accessing the map.No direct changes to this method are required unless you want to enforce type-safety when writing back the collection.");
        this.getController().fireOrder(OrderType.SHOW_CONFIRM_MODAL);
//        Helper.showAlert(Alert.AlertType.ERROR,"Title","This is a message!This is a message!This is a message!This is a message!This is a message!This is a message!This is a message!This is a message!This is a message!This is a message!");
    }
}
