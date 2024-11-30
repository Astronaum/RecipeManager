package dp.grp4.views;

import dp.grp4.controllers.IngredientModalController;
import dp.grp4.helpers.Helper;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.exceptions.DBException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class IngredientModalView extends ModalView {

    private static final int MODAL_WIDTH = 500, MODAL_HEIGHT = 500;

    @FXML
    private TextField nameField;
    @FXML
    private TextField stockField;
    @FXML
    private ComboBox<Ingredient.Unit> unitComboBox;
    @FXML
    private DatePicker expirationDatePicker;
    @FXML
    private Label modalTitle;

    private Runnable onConfirmAction;
    private Ingredient ingredientToEdit = null;
    private final IngredientDAO ingredientDAO = IngredientDAO.getInstance();

    public static IngredientModalView create(ViewsManager viewsManager) throws IOException {
        IngredientModalView view = (IngredientModalView) ModalView.create(viewsManager, "ingredient_modal.fxml");
        IngredientModalController controller = IngredientModalController.create(viewsManager);
        view.setController(controller);
        return view;
    }
    /**
     * Définit l'ingrédient à modifier.
     * Si null, le modal sera utilisé pour ajouter un nouvel ingrédient.
     */
    public void setIngredientToEdit(Ingredient ingredient) {
        this.ingredientToEdit = ingredient;
        if (ingredient != null) {
            // Pré-remplir les champs avec les données existantes
            setName(ingredient.getName());
            setStock(ingredient.getStock());
            setUnit(ingredient.getUnit());
            setExpirationDate(ingredient.getExpirationDate());
            modalTitle.setText("Modifier Ingrédient");
        }else {
            resetForm();
            modalTitle.setText("Ajouter Ingrédient");
        }
    }
    public void setName(String name) {
        this.nameField.setText(name);
    }

    public void setStock(int stock) {
        this.stockField.setText(String.valueOf(stock));
    }

    public void setUnit(Ingredient.Unit unit) {
        this.unitComboBox.setValue(unit);
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDatePicker.setValue(expirationDate);
    }

    public String getName() {
        return nameField.getText();
    }

    @FXML
    private void onConfirm() {
        try {
            if (nameField.getText().isEmpty()) {
                throw new IllegalArgumentException("Le nom est requis.");
            }

            int stock = Integer.parseInt(stockField.getText());
            /*if (stock <= 0) {
                throw new IllegalArgumentException("Le stock doit être supérieur à 0.");
            }*/

            Ingredient.Unit unit = unitComboBox.getValue();
            if (unit == null) {
                throw new IllegalArgumentException("Veuillez sélectionner une unité.");
            }

            if (ingredientToEdit == null) {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(nameField.getText());
                newIngredient.setStock(stock);
                newIngredient.setUnit(unit);
                newIngredient.setExpirationDate(expirationDatePicker.getValue());

                ingredientDAO.add(newIngredient);
            } else {
                // Modifier l'ingrédient existant
                ingredientToEdit.setName(nameField.getText());
                ingredientToEdit.setStock(stock);
                ingredientToEdit.setUnit(unit);
                ingredientToEdit.setExpirationDate(expirationDatePicker.getValue());

                ingredientDAO.update(ingredientToEdit);
            }

            close(); // Fermer le modal après l'action
        } catch (NumberFormatException e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Erreur", "Le stock doit être un entier valide.");
        } catch (IllegalArgumentException e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        } catch (DBException e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    private void resetForm() {
        nameField.clear();
        stockField.clear();
        unitComboBox.setValue(null);
        expirationDatePicker.setValue(null);
        ingredientToEdit = null;
    }
    @FXML
    private void closeModal() {
        close();
    }

    @Override
    public void open() {
        Stage modalStage = this.initStage();
        modalStage.setResizable(false);
        modalStage.setWidth(MODAL_WIDTH);
        modalStage.setHeight(MODAL_HEIGHT);
        unitComboBox.setItems(FXCollections.observableArrayList(Ingredient.Unit.values()));
        modalStage.showAndWait();
    }
}

