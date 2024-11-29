package dp.grp4.views;

import dp.grp4.controllers.RecipesController;
import dp.grp4.exceptions.DBException;
import dp.grp4.helpers.Helper;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.entities.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class RecipesView extends InteractiveView implements InitializableView{

    @FXML
    private TableView<Recipe> recipesTable;

    @FXML
    private TableColumn<Recipe, String> nameColumn;
    @FXML
    private TableColumn<Recipe, String> idColumn;
    @FXML
    private TableColumn<Recipe, Integer> prepTimeColumn;
    @FXML
    private TableColumn<Recipe, Integer> noteColumn;

    @FXML
    private TableColumn<Recipe, String> commentColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Difficulty> difficultyColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Category> categoryColumn;

    @FXML
    private TableColumn<Recipe, Void> favoriteColumn;

    @FXML
    private TableColumn<Recipe, Void> actionsColumn;

    @FXML
    private TextField searchNameField;

    @FXML
    private CheckBox favoritesCheckBox;

    @FXML
    private ComboBox<Recipe.Category> categoryComboBox;

    @FXML
    private ComboBox<Recipe.Difficulty> difficultyComboBox;

    @FXML
    private TextField maxPrepTimeField;

    @FXML
    private RadioButton andRadioButton;

    @FXML
    private RadioButton orRadioButton;

    @FXML
    private ToggleGroup logicToggleGroup;

    private final RecipeDAO recipeDAO = RecipeDAO.getInstance();
    private final ObservableList<Recipe> recipesList = FXCollections.observableArrayList();

    public static RecipesView create(ViewsManager viewsManager) throws IOException {
        RecipesController recipesController = RecipesController.create(viewsManager);
        RecipesView recipesView = (RecipesView) InteractiveView.getView(viewsManager, "recipes.fxml");
        recipesView.setController(recipesController);
        return recipesView;
    }

    @FXML
    public void initialize() {
        Helper.setTableViewProperties(recipesTable);
        categoryComboBox.setItems(FXCollections.observableArrayList(Recipe.Category.values()));
        difficultyComboBox.setItems(FXCollections.observableArrayList(Recipe.Difficulty.values()));

        logicToggleGroup = new ToggleGroup();
        andRadioButton.setToggleGroup(logicToggleGroup);
        orRadioButton.setToggleGroup(logicToggleGroup);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        prepTimeColumn.setCellValueFactory(new PropertyValueFactory<>("preparationTime"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        favoriteColumn.setCellFactory(column -> new TableCell<>() {
            private final Button favoriteButton = new Button("☆");

            {
                favoriteButton.setOnAction(event -> {
                    Recipe recipe = getTableView().getItems().get(getIndex());
                    recipe.setFavourite(!recipe.isFavourite());
                    try {
                        RecipeDAO.getInstance().update(recipe);
                    } catch (DBException e) {
                        throw new RuntimeException(e);
                    }
                    updateFavoriteButton(recipe);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Recipe recipe = getTableView().getItems().get(getIndex());
                    updateFavoriteButton(recipe);
                    setGraphic(favoriteButton);
                }
            }

            private void updateFavoriteButton(Recipe recipe) {
                favoriteButton.setText(recipe.isFavourite() ? "★" : "☆");
                if (favoritesCheckBox.isSelected()) {
                    refreshRecipesTable();
                }
            }
        });

        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("✎");
            private final Button deleteButton = new Button("🗑");
            private final Button detailsButton = new Button("📝");
            private final HBox actionButtons = new HBox(editButton, deleteButton, detailsButton);

            {
                editButton.setOnAction(event -> {
                    Recipe recipe = getTableView().getItems().get(getIndex());

                    RecipeModalView modalView = Helper.getViewInstance(RecipeModalView.class);
                    modalView.setRecipeToEdit(recipe);
                    modalView.open();
                    refreshRecipesTable();
                });

                deleteButton.setOnAction(event -> {
                    Recipe recipe = getTableView().getItems().get(getIndex());

                    ConfirmModalView modalView = Helper.getViewInstance(ConfirmModalView.class);
                    modalView.setModalText("Êtes-vous sûr de vouloir supprimer la recette avec ID = "
                            + recipe.getId() + " : " + recipe.getName() + " ?");

                    modalView.setOnConfirmAction(() -> {
                        recipeDAO.delete(recipe.getId());
                        refreshRecipesTable();
                        Helper.showAlert(Alert.AlertType.INFORMATION, "Succès",
                                "La recette a été supprimée avec succès.");
                    });

                    modalView.open();
                });

                detailsButton.setOnAction(event -> {
                    Recipe recipe = getTableView().getItems().get(getIndex());
                    RecipeDetailsModalView modalView = Helper.getViewInstance(RecipeDetailsModalView.class);
                    if (modalView == null) {
                        Helper.showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la vue des détails de la recette.");
                        return;
                    }
                    modalView.setRecipe(recipe);
                    modalView.open();

                });


                actionButtons.setSpacing(5);
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

        refreshRecipesTable();
        recipesTable.refresh();
    }

    @FXML
    private void onAddRecipe() {
        RecipeModalView modalView = Helper.getViewInstance(RecipeModalView.class);
        modalView.setRecipeToEdit(null);
        modalView.open();
        refreshRecipesTable();
    }


    @FXML
    private void onSearchRecipes() {
        String name = searchNameField.getText().trim();
        Recipe.Category category = categoryComboBox.getValue();
        Recipe.Difficulty difficulty = difficultyComboBox.getValue();
        boolean favoriteOnly = favoritesCheckBox.isSelected();
        Integer maxPrepTime = null;

        if (!maxPrepTimeField.getText().isEmpty()) {
            try {
                maxPrepTime = Integer.parseInt(maxPrepTimeField.getText().trim());
            } catch (NumberFormatException e) {
                maxPrepTime = null;
            }
        }

        recipesList.clear();
        boolean useAndLogic = andRadioButton.isSelected();
        List<Recipe> filteredRecipes = recipeDAO.filter(
                name.isEmpty() ? null : name,
                category,
                difficulty,
                favoriteOnly ? Boolean.TRUE : null,
                maxPrepTime,
                useAndLogic
        );

        recipesList.setAll(filteredRecipes);
        recipesTable.setItems(recipesList);
    }


    private void refreshRecipesTable() {
        List<Recipe> recipes = recipeDAO.getAll();
        recipesList.setAll(recipes);
        recipesTable.setItems(recipesList);
    }

    public RecipesController getController() {
        return (RecipesController) super.getController();
    }

    @FXML
    private void onViewFeasibleRecipes() {
        this.getController().onViewFeasibleRecipes();
    }

    @FXML
    private void onShowFavorites() {
        this.getController().onShowFavorites();
    }

    public void goHome(ActionEvent e) {
        this.getController().gotoHome();
    }


    public void goToIngredients(MouseEvent mouseEvent) {
        this.getController().goToIngredients();
    }
}
