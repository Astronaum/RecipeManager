package dp.grp4.views;

import dp.grp4.controllers.RecipeModalController;
import dp.grp4.helpers.Helper;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class RecipeModalView extends ModalView {

    @FXML
    private Label modalTitle;

    @FXML
    private TextField nameField;

    @FXML
    private TextField prepTimeField;

    @FXML
    private TextField noteField;

    @FXML
    private TextArea commentField;

    @FXML
    private ComboBox<Recipe.Difficulty> difficultyComboBox;

    @FXML
    private ComboBox<Recipe.Category> categoryComboBox;

    @FXML
    private ComboBox<Ingredient> ingredientComboBox;

    @FXML
    private TextField ingredientQuantityField;

    @FXML
    private TableView<Map.Entry<Ingredient, Integer>> ingredientTable;

    @FXML
    private TableColumn<Map.Entry<Ingredient, Integer>, String> ingredientNameColumn;

    @FXML
    private TableColumn<Map.Entry<Ingredient, Integer>, Integer> ingredientQuantityColumn;

    @FXML
    private TableColumn<Map.Entry<Ingredient, Integer>, Void> ingredientActionColumn;

    @FXML
    private TextArea instructionsField;

    private Recipe recipeToEdit = null;
    private final Map<Ingredient, Integer> ingredientsMap = new HashMap<>();
    private final ObservableList<Map.Entry<Ingredient, Integer>> ingredientTableData = FXCollections.observableArrayList();

    public static RecipeModalView create(ViewsManager viewsManager) throws IOException {
        RecipeModalView view = (RecipeModalView) ModalView.create(viewsManager, "recipe_modal.fxml");
        RecipeModalController controller = RecipeModalController.create(viewsManager);
        view.setController(controller);
        return view;
    }

    @Override
    public void open() {
        Stage modalStage = this.initStage();
        modalStage.setResizable(false);
        modalStage.setWidth(600);
        modalStage.setHeight(600);

        difficultyComboBox.setItems(FXCollections.observableArrayList(Recipe.Difficulty.values()));
        categoryComboBox.setItems(FXCollections.observableArrayList(Recipe.Category.values()));

        try {
            List<Ingredient> allIngredients = IngredientDAO.getInstance().getAll();
            ingredientComboBox.setItems(FXCollections.observableArrayList(allIngredients));
        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les ingrédients : " + e.getMessage());
        }

        ingredientNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey().getName())
        );

        ingredientQuantityColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getValue())
        );

        ingredientActionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button("🗑");

            {
                deleteButton.setOnAction(event -> {
                    Map.Entry<Ingredient, Integer> entry = getTableView().getItems().get(getIndex());
                    Ingredient ingredient = entry.getKey();
                    ingredientsMap.remove(ingredient);
                    ingredientTableData.remove(entry);
                    ingredientTable.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        ingredientTable.setItems(ingredientTableData);
        modalStage.showAndWait();
    }

    public void setRecipeToEdit(Recipe recipe) {
        this.recipeToEdit = recipe;

        if (recipe != null) {
            modalTitle.setText("Modifier une Recette");
            nameField.setText(recipe.getName());
            prepTimeField.setText(String.valueOf(recipe.getPreparationTime()));
            noteField.setText(String.valueOf(recipe.getNote()));
            commentField.setText(recipe.getComment());
            difficultyComboBox.setValue(recipe.getDifficulty());
            categoryComboBox.setValue(recipe.getCategory());
            instructionsField.setText(recipe.getInstructionsList() != null
                    ? String.join("\n", recipe.getInstructionsList())
                    : "");

            ingredientsMap.clear();
            ingredientTableData.clear();
            if (recipe.getIngredients() != null) {
                for (Recipe.IngredientQuantity iq : recipe.getIngredients()) {
                    Ingredient ingredient = ingredientComboBox.getItems().stream()
                            .filter(i -> i.getId() == iq.getId())
                            .findFirst()
                            .orElse(null);
                    if (ingredient != null) {
                        ingredientsMap.put(ingredient, iq.getQuantity());
                        ingredientTableData.add(Map.entry(ingredient, iq.getQuantity()));
                    }
                }
            }
            ingredientTable.setItems(ingredientTableData);
        } else {
            resetForm();
        }
    }

    private void resetForm() {
        modalTitle.setText("Ajouter une Recette");
        nameField.clear();
        prepTimeField.clear();
        noteField.clear();
        commentField.clear();
        difficultyComboBox.setValue(null);
        categoryComboBox.setValue(null);
        instructionsField.clear();
        ingredientsMap.clear();
        ingredientTableData.clear();
        ingredientTable.setItems(ingredientTableData);
    }

    @FXML
    private void onAddIngredientToList() {
        Ingredient selectedIngredient = ingredientComboBox.getValue();
        String quantityText = ingredientQuantityField.getText();

        if (selectedIngredient == null || quantityText.isEmpty()) {
            Helper.showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner un ingrédient et saisir une quantité.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            if (ingredientsMap.containsKey(selectedIngredient)) {
                Helper.showAlert(Alert.AlertType.WARNING, "Erreur", "Cet ingrédient est déjà ajouté.");
                return;
            }

            ingredientsMap.put(selectedIngredient, quantity);
            ingredientTableData.add(Map.entry(selectedIngredient, quantity));
            ingredientTable.setItems(FXCollections.observableArrayList(ingredientTableData));

            resetIngredientSelection();
        } catch (NumberFormatException e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité doit être un entier valide.");
        }
    }

    private void resetIngredientSelection() {
        ingredientComboBox.setValue(null);
        ingredientQuantityField.clear();
    }

    @FXML
    private void onAddNewIngredient() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouvel ingrédient");
        dialog.setHeaderText("Ajouter un nouvel ingrédient");
        dialog.setContentText("Nom de l'ingrédient :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name.isBlank()) {
                Helper.showAlert(Alert.AlertType.WARNING, "Erreur", "Le nom de l'ingrédient ne peut pas être vide.");
                return;
            }

            Ingredient newIngredient = new Ingredient();
            newIngredient.setName(name.trim());
            newIngredient.setStock(0);
            newIngredient.setUnit(null);
            newIngredient.setExpirationDate(null);

            try {
                IngredientDAO.getInstance().add(newIngredient);

                List<Ingredient> allIngredients = IngredientDAO.getInstance().getAll();
                ingredientComboBox.setItems(FXCollections.observableArrayList(allIngredients));
                ingredientComboBox.setValue(newIngredient);

                Helper.showAlert(Alert.AlertType.INFORMATION, "Succès", "L'ingrédient a été ajouté avec succès.");
            } catch (Exception e) {
                Helper.showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter l'ingrédient : " + e.getMessage());
            }
        });
    }

    @FXML
    private void onConfirm() {
        try {
            if (nameField.getText().isEmpty() || prepTimeField.getText().isEmpty()) {
                throw new IllegalArgumentException("Les champs Nom et Temps de préparation sont obligatoires.");
            }

            Recipe recipe = (recipeToEdit == null) ? new Recipe() : recipeToEdit;
            recipe.setName(nameField.getText());
            recipe.setNote(Integer.parseInt(String.valueOf(Integer.parseInt(noteField.getText()))));
            recipe.setComment(commentField.getText());
            recipe.setPreparationTime(Integer.parseInt(prepTimeField.getText()));
            recipe.setDifficulty(difficultyComboBox.getValue());
            recipe.setCategory(categoryComboBox.getValue());
            recipe.setInstructionsList(List.of(instructionsField.getText().split("\n")));
            recipe.setIngredients(new ArrayList<>(ingredientsMap.entrySet().stream()
                    .map(entry -> new Recipe.IngredientQuantity(entry.getKey().getId(), entry.getValue()))
                    .toList()));

            if (recipeToEdit == null) {
                RecipeDAO.getInstance().add(recipe);
            } else {
                RecipeDAO.getInstance().update(recipe);
            }

            close();
        } catch (IllegalArgumentException e) {
            Helper.showAlert(Alert.AlertType.WARNING, "Erreur", e.getMessage());
        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    @FXML
    private void closeModal() {
        close();
    }
}
