package dp.grp4.views;

import dp.grp4.exceptions.ExceptionHandler;
import dp.grp4.models.db.JsonDB;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsView extends ModalView {
    private static final int MODAL_WIDTH = 400, MODAL_HEIGHT = 250;
    public TextField input;

    public static SettingsView create(ViewsManager viewsManager) throws IOException {
        return (SettingsView) ModalView.create(viewsManager, "settings.fxml");
    }

    @Override
    public void open() {
        Stage modalStage = this.initStage();
        modalStage.setResizable(false);
        modalStage.setWidth(MODAL_WIDTH);
        modalStage.setHeight(MODAL_HEIGHT);
        input.setText(JsonDB.getDBFolder());
        modalStage.showAndWait();
    }

    @FXML
    private void onApply() {
        JsonDB.setDBFolder(input.getText());
        ViewsManager.getInstance().reloadViews();
        close();
    }

}
