package dp.grp4.controllers;

import dp.grp4.views.ViewsManager;
import javafx.scene.Parent;

import java.io.IOException;

public interface InteractiveView {
    Controller getController();
    void setController(Controller controller);
}
