package dp.grp4.views;

import dp.grp4.controllers.IngredientsController;
import dp.grp4.controllers.RecipesController;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.dao.IngredientIDAO;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.orders.OrderType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;

public class IngredientsView extends InteractiveView {

    @FXML
    private TableView<Ingredient> ingredientsTable;

    @FXML
    private TableColumn<Ingredient, String> nameColumn;

    @FXML
    private TableColumn<Ingredient, Integer> stockColumn;

    @FXML
    private TableColumn<Ingredient, Ingredient.Unit> unitColumn;

    @FXML
    private TableColumn<Ingredient, String> expirationColumn;

    @FXML
    private TableColumn<Ingredient, Void> actionColumn;
    @FXML
    private TextField nameField;

    @FXML
    private TextField stockField;

    @FXML
    private ComboBox<Ingredient.Unit> unitComboBox;
    @FXML
    private Button addOrSaveButton;

    @FXML
    private DatePicker expirationDatePicker;

    private final IngredientIDAO ingredientDAO = IngredientDAO.getInstance();
    private Ingredient ingredientBeingEdited = null;
    public static IngredientsView create(ViewsManager viewsManager) throws IOException {
        IngredientsController ingredientsController=IngredientsController.create(viewsManager);
        IngredientsView ingredientsView = (IngredientsView) InteractiveView.getView(viewsManager, "ingredients.fxml");
        ingredientsView.setController(ingredientsController);

        return ingredientsView;
    }

    @FXML
    private void initialize() {
        System.out.println("Initializing IngredientsView...");

        // Configurer les colonnes
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        addActionButtons();
        // Charger les données initiales
        refreshIngredientsTable();
        unitComboBox.getItems().setAll(Ingredient.Unit.values());
    }
    private void addActionButtons() {
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Configurer le bouton "Modifier"
                editButton.setOnAction(event -> {
                    Ingredient ingredient = getTableView().getItems().get(getIndex());
                    loadIngredientIntoForm(ingredient); // Charger les détails dans le formulaire
                    System.out.println("Modification en cours pour : " + ingredient.getName());
                });

                // Configurer le bouton "Supprimer"
                deleteButton.setOnAction(event -> {
                    Ingredient ingredient = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(ingredient); // Confirmation avant suppression
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }
    private void loadIngredientIntoForm(Ingredient ingredient) {
        this.ingredientBeingEdited = ingredient; // Marquer l'ingrédient comme en cours d'édition

        nameField.setText(ingredient.getName());
        stockField.setText(String.valueOf(ingredient.getStock()));
        unitComboBox.setValue(ingredient.getUnit());
        expirationDatePicker.setValue(ingredient.getExpirationDate());

        // Changer le texte du bouton en "Enregistrer"
        addOrSaveButton.setText("Enregistrer");
    }

    private void showDeleteConfirmation(Ingredient ingredient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet ingrédient ?");
        alert.setContentText("Ingrédient : " + ingredient.getName());

        // Si l'utilisateur confirme, on supprime
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ingredientDAO.delete(ingredient.getId());
                refreshIngredientsTable();
                System.out.println("Ingrédient supprimé : " + ingredient.getName());
            }
        });
    }

    private void refreshIngredientsTable() {
        System.out.println("Refreshing ingredients table...");
        ingredientsTable.setItems(FXCollections.observableArrayList(ingredientDAO.getAll()));
        System.out.println("Ingredients table refreshed.");
    }
    @FXML
    private void onAddIngredient() {
        try {
            // Valider les champs
            String name = nameField.getText();
            int stock = Integer.parseInt(stockField.getText());
            Ingredient.Unit unit = unitComboBox.getValue();
            LocalDate expirationDate = expirationDatePicker.getValue();

            if (name.isEmpty() || unit == null || expirationDate == null) {
                System.out.println("Veuillez remplir tous les champs !");
                return;
            }

            if (ingredientBeingEdited != null) {
                // Modification d'un ingrédient existant
                ingredientBeingEdited.setName(name);
                ingredientBeingEdited.setStock(stock);
                ingredientBeingEdited.setUnit(unit);
                ingredientBeingEdited.setExpirationDate(expirationDate);

                ingredientDAO.update(ingredientBeingEdited);
                System.out.println("Ingrédient modifié : " + ingredientBeingEdited.getName());

                // Forcer la mise à jour visuelle
                ingredientsTable.refresh();

                // Réinitialiser le mode d'édition
                ingredientBeingEdited = null;

                // Remettre le texte du bouton à "Ajouter"
                addOrSaveButton.setText("Ajouter");
            } else {
                // Ajout d'un nouvel ingrédient
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(name);
                newIngredient.setStock(stock);
                newIngredient.setUnit(unit);
                newIngredient.setExpirationDate(expirationDate);

                ingredientDAO.add(newIngredient);
                System.out.println("Ingrédient ajouté : " + newIngredient.getName());
            }

            // Rafraîchir la table pour inclure les modifications ou les ajouts
            refreshIngredientsTable();

            // Réinitialiser les champs
            clearForm();
        } catch (NumberFormatException e) {
            System.out.println("Erreur : La quantité doit être un nombre !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout/modification de l'ingrédient : " + e.getMessage());
        }
    }


    private void clearForm() {
        nameField.clear();
        stockField.clear();
        unitComboBox.setValue(null);
        expirationDatePicker.setValue(null);

        ingredientBeingEdited = null; // Sortir du mode d'édition

        // Remettre le texte du bouton à "Ajouter"
        addOrSaveButton.setText("Ajouter");
    }



    public IngredientsController getController(){
        return (IngredientsController) super.getController();
    }

    public void gotoHome(MouseEvent e) {
        this.getController().fireOrder(OrderType.SHOW_HOME);
    }
}
