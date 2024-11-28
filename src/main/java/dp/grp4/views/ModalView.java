package dp.grp4.views;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ModalView extends InteractiveView {
    private Stage parentStage;
    private Stage stage;

    protected static ModalView create(ViewsManager viewsManager,String fxmlFileName) throws IOException {
        ModalView view=(ModalView) InteractiveView.getView(viewsManager,fxmlFileName);
        view.parentStage=viewsManager.getStage();
        return view;
    }
    protected Stage initStage(){
        this.stage = new Stage();
        this.stage.initModality(Modality.WINDOW_MODAL);
        this.stage.initOwner(this.parentStage);
        this.stage.setScene(this.scene);
        return this.stage;
    }

    public void open(){}

    protected void close() {
        this.stage.close();
        this.stage=null;
    }

}