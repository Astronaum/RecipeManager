package dp.grp4.views;

import dp.grp4.controllers.IngredientsController;
import dp.grp4.helpers.Helper;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.entities.Ingredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class IngredientsView extends InteractiveView implements InitializableView {

    @FXML
    private TableView<Ingredient> ingredientsTable;

    @FXML
    private TableColumn<Ingredient, String> idColumn;

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

    private final IngredientDAO ingredientDAO = IngredientDAO.getInstance();
    private final ObservableList<Ingredient> ingredientsList = FXCollections.observableArrayList();
    public static IngredientsView create(ViewsManager viewsManager) throws IOException {
        IngredientsController ingredientsController = IngredientsController.create(viewsManager);
        IngredientsView ingredientsView = (IngredientsView) InteractiveView.getView(viewsManager, "ingredients.fxml");
        ingredientsView.setController(ingredientsController);
        return ingredientsView;
    }

    @FXML
    public void initialize() {
        Helper.setTableViewProperties(ingredientsTable);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("🖉");
            private final Button deleteButton = new Button("🗑");
            private final HBox actionButtons = new HBox(editButton, deleteButton);

            {
                actionButtons.setSpacing(10);
                editButton.setOnAction(event -> {
                    Ingredient ingredient = getTableView().getItems().get(getIndex());
                    onModifyIngredient(ingredient);
                });

                deleteButton.setOnAction(event -> {
                    Ingredient ingredient = getTableView().getItems().get(getIndex());
                    onDeleteIngredient(ingredient);
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButtons);
                }
            }
        });

        refreshIngredientsTable();
    }
    private void onDeleteIngredient(Ingredient ingredient) {
        ConfirmModalView modalView = Helper.getViewInstance(ConfirmModalView.class);

        modalView.setModalText("Êtes-vous sûr de vouloir supprimer l'ingrédient avec ID = "
                + ingredient.getId() + " : " + ingredient.getName() + " ?");

        modalView.setOnConfirmAction(() -> {
            ingredientDAO.delete(ingredient.getId());

            refreshIngredientsTable();

            Helper.showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "L'ingrédient a été supprimé avec succès.");
        });

        modalView.open();
    }


    private void onModifyIngredient(Ingredient ingredient) {
        IngredientModalView modalView = Helper.getViewInstance(IngredientModalView.class);
        modalView.setIngredientToEdit(ingredient);
        modalView.open();
        refreshIngredientsTable();
    }

    void refreshIngredientsTable() {
        List<Ingredient> ingredients = ingredientDAO.getAll();
        ingredientsList.setAll(ingredients);
        ingredientsTable.setItems(ingredientsList);
    }
    @FXML
    private void onAddIngredient() {
        IngredientModalView modalView = Helper.getViewInstance(IngredientModalView.class);
        modalView.setIngredientToEdit(null);

        modalView.open();
        refreshIngredientsTable();
    }



    public IngredientsController getController() {
        return (IngredientsController) super.getController();
    }

    public void gotoHome(ActionEvent e) {
        this.getController().gotoHome();
    }

    @FXML
    private void gotoRecipes() {
        this.getController().gotoRecipes();
    }


}
