package dp.grp4.views;

import dp.grp4.controllers.ModalController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ModalView extends InteractiveView {
    private static final int MODAL_WIDTH=400, MODAL_HEIGHT=300;
    private Stage parentStage;
    private Stage stage;

    @FXML
    private Text modalText;

    public static ModalView create(ViewsManager viewsManager) throws IOException {
        ModalController controller=ModalController.create(viewsManager);
        ModalView view=(ModalView) InteractiveView.getView(viewsManager,"modal.fxml");
        view.setController(controller);
        view.parentStage=viewsManager.getStage();
        return view;
    }

    public ModalController getController(){
        return (ModalController) super.getController();
    }
    public void open() {
        this.stage = new Stage();
        this.stage.setResizable(false);
        this.stage.setWidth(MODAL_WIDTH);
        this.stage.setHeight(MODAL_HEIGHT);
        this.stage.initModality(Modality.WINDOW_MODAL);
        this.stage.initOwner(this.parentStage);
        this.stage.setScene(this.scene);
        this.stage.showAndWait();
    }

    public void close(MouseEvent e) {
        this.stage.close();
        this.stage=null;
    }

    public void setModalText(String modalText) {
        this.modalText.setText(modalText);
    }
}
