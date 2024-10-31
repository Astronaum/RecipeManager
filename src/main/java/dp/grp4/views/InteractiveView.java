package dp.grp4.views;

import dp.grp4.controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public abstract class InteractiveView {
    private static final int WINDOW_WIDTH=600, WINDOW_HEIGHT=400;

    @FXML
    private Parent rootPane;
    private Scene scene;
    private Controller controller;

    protected static InteractiveView getView(ViewsManager viewsManager,String fxmlFileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InteractiveView.class.getResource(fxmlFileName));
        fxmlLoader.load();
        InteractiveView view = fxmlLoader.getController();
        viewsManager.addView(view);
        view.initialiseScene();
        return view;
    }

    public void initialiseScene() {
        this.scene = new Scene(rootPane, WINDOW_WIDTH, WINDOW_HEIGHT);
    }
    public Scene getScene() {
        return scene;
    }

    public Controller getController() {
        return this.controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
