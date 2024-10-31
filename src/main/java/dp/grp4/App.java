package dp.grp4;

import dp.grp4.controllers.Controller;
import dp.grp4.views.ViewsManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ViewsManager viewsManager = ViewsManager.getInstance(stage);
        Controller.run(viewsManager);
    }

    public static void main(String[] args) {
        launch();
    }

}