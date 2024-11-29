package dp.grp4;

import dp.grp4.exceptions.ExceptionHandler;
import dp.grp4.views.ViewsManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        ExceptionHandler.context(()->{
            ViewsManager.init(stage);
            ViewsManager.getInstance().launch();
        });
    }
    public static void main(String[] args) {
        launch();
    }
}