package dp.grp4.views;

import dp.grp4.controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public abstract class InteractiveView {
    private static final int WINDOW_WIDTH=1000, WINDOW_HEIGHT=600;

    @FXML
    private Parent root;
    protected Scene scene;
    private Controller controller;

    protected static InteractiveView getView(ViewsManager viewsManager,String fxmlFileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InteractiveView.class.getResource(fxmlFileName));
        fxmlLoader.load();
        InteractiveView view = fxmlLoader.getController();
        viewsManager.addView(view);
        view.initialiseScene();
        return view;
    }
    private void initialiseScene() {
        this.scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }
    public Scene getScene() {
        return this.scene;
    }
    public Controller getController() {
        return this.controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
