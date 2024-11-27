package dp.grp4.views;

import dp.grp4.controllers.ModalController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    @FXML
    private TextArea detailsArea;
    @FXML
    private HBox inputFields;
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField ingredientQuantityField;
    @FXML
    private Button actionButton;

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


    /**
     * Cette méthode permet de personnaliser le texte du modal (ex : pour afficher des détails)
     */
    public void setModalText(String text) {
        this.modalText.setText(text);
    }

    /**
     * Cette méthode permet de définir le contenu de la zone des détails
     */
    public void setDetails(String details) {
        this.detailsArea.setText(details);
    }
    // Public getter for ingredient name field
    public TextField getIngredientNameField() {
        return ingredientNameField;
    }

    // Public getter for ingredient quantity field
    public TextField getIngredientQuantityField() {
        return ingredientQuantityField;
    }

    /**
     * Cette méthode permet de définir la visibilité des champs d'ajout d'ingrédients
     */
    public void showIngredientInputFields(boolean show) {
        this.inputFields.setVisible(show);
    }

    /**
     * Cette méthode définit l'action du bouton du modal (pour ajouter ou modifier un ingrédient)
     */
    public void setActionButton(String buttonText, Runnable action) {
        this.actionButton.setText(buttonText);
        this.actionButton.setOnAction(event -> action.run());
    }}
