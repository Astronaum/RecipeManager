package dp.grp4;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage){
        ViewsManager viewsManager = ViewsManager.getInstance(stage);
        viewsManager.processOrder(OrderType.SHOW_HOME);
    }
    public static void main(String[] args) {
        launch();
    }

}