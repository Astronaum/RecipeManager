package dp.grp4.views;

import dp.grp4.controllers.ConfirmModalController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmModalView extends ModalView{
    private static final int MODAL_WIDTH=400, MODAL_HEIGHT=250;

    @FXML
    private TextArea errorMessage;

    public static ConfirmModalView create(ViewsManager viewsManager) throws IOException {
        ConfirmModalView view= (ConfirmModalView) ModalView.create(viewsManager,"confirm_modal.fxml");
        ConfirmModalController controller= ConfirmModalController.create(viewsManager);
        view.setController(controller);
        return view;
    }
    @Override
    public void open(){
        Stage modalStage=this.initStage();
        modalStage.setResizable(false);
        modalStage.setWidth(MODAL_WIDTH);
        modalStage.setHeight(MODAL_HEIGHT);
        modalStage.showAndWait();
    }
    public void setModalText(String modalText) {
        this.errorMessage.setText(modalText);
    }

    public void closeModal(MouseEvent e){
        this.close();
    }
}
