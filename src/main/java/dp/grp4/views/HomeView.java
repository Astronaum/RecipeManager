package dp.grp4.views;

import dp.grp4.controllers.HomeController;
import dp.grp4.orders.OrderType;
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
}
