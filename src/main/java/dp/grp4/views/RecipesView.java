package dp.grp4.views;

import dp.grp4.controllers.RecipesController;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class RecipesView extends InteractiveView {

    @FXML
    private TableView<Recipe> recipesTable;

    @FXML
    private TableColumn<Recipe, String> nameColumn;

    @FXML
    private TableColumn<Recipe, Integer> prepTimeColumn;

    @FXML
    private TableColumn<Recipe, String> difficultyColumn;

    @FXML
    private TableColumn<Recipe, String> categoryColumn;

    @FXML
    private TableColumn<Recipe, Void> actionColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField prepTimeField;

    @FXML
    private ComboBox<Recipe.Difficulty> difficultyComboBox;

    @FXML
    private ComboBox<Recipe.Category> categoryComboBox;

    @FXML
    private ListView<Ingredient> ingredientListView;  // ListView for ingredients

    @FXML
    private TextArea instructionsTextArea;

    @FXML
    private Button addButton;

    private final RecipeDAO recipeDAO = RecipeDAO.getInstance();
    private final IngredientDAO ingredientDAO = IngredientDAO.getInstance(); // DAO to manage ingredients

    public static RecipesView create(ViewsManager viewsManager) throws IOException {
        RecipesController recipesController = RecipesController.create(viewsManager);
        RecipesView recipesView = (RecipesView) InteractiveView.getView(viewsManager, "recipes.fxml");
        recipesView.setController(recipesController);
        return recipesView;
    }

    public RecipesController getController() {
        return (RecipesController) super.getController();
    }

    @FXML
    private void initialize() {
        System.out.println("Initializing RecipesView...");

        // Configure table columns with properties
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        prepTimeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getPreparationTime()).asObject());
        difficultyColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDifficulty().toString()));
        categoryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory().toString()));

        // Add action buttons (modify, delete, view details)
        addActionButtons();

        // Initialize comboBoxes for difficulty and category
        difficultyComboBox.getItems().setAll(Recipe.Difficulty.values());
        categoryComboBox.getItems().setAll(Recipe.Category.values());

        // Initialize ListView for ingredients
        ingredientListView.setItems(FXCollections.observableArrayList(ingredientDAO.getAll()));

        // Set the ListView to display ingredient names
        ingredientListView.setCellFactory(param -> new ListCell<Ingredient>() {
            @Override
            protected void updateItem(Ingredient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());  // Display the ingredient's name
                }
            }
        });

        ingredientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multiple selections

        // Refresh the recipe table
        refreshRecipesTable();
    }


    private void addActionButtons() {
        // Create "cell factory" for action column (modify, delete, view details buttons)
        actionColumn.setCellFactory(param -> new TableCell<Recipe, Void>() {
            private final Button modifyButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final Button viewDetailsButton = new Button("Voir les détails");

            {
                modifyButton.setOnAction(event -> onModifyRecipe(getTableRow().getItem()));
                deleteButton.setOnAction(event -> onDeleteRecipe(getTableRow().getItem()));
                viewDetailsButton.setOnAction(event -> onViewDetails(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(modifyButton, deleteButton, viewDetailsButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    private void onViewDetails(Recipe recipe) {
        if (recipe != null) {
            StringBuilder details = new StringBuilder();
            details.append("Nom: ").append(recipe.getName()).append("\n");
            details.append("Temps de préparation: ").append(recipe.getPreparationTime()).append(" minutes\n");
            details.append("Difficulté: ").append(recipe.getDifficulty()).append("\n");
            details.append("Catégorie: ").append(recipe.getCategory()).append("\n");
            details.append("\nIngrédients:\n");

            // Check if ingredient list is null and replace it with an empty list if necessary
            if (recipe.getIngredients() != null) {
                for (Recipe.IngredientQuantity ingredient : recipe.getIngredients()) {
                    details.append("- ").append(ingredient.getQuantity()).append(" of ").append(ingredient.getId()).append("\n");
                }
            } else {
                details.append("Aucun ingrédient disponible.\n");
            }

            details.append("\nInstructions:\n");
            if (recipe.getInstructionsList() != null) {
                for (String instruction : recipe.getInstructionsList()) {
                    details.append("- ").append(instruction).append("\n");
                }
            } else {
                details.append("Aucune instruction disponible.\n");
            }

            // Show details in an alert or modal
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Détails de la recette");
            alert.setHeaderText(recipe.getName());
            alert.setContentText(details.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private void onDeleteRecipe(Recipe recipe) {
        if (recipe != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette recette ?");
            alert.setContentText("Cette action est irréversible.");


        }
    }

    @FXML
    private void onModifyRecipe(Recipe recipe) {
        if (recipe != null) {
            // Fill form fields with existing values
            nameField.setText(recipe.getName());
            prepTimeField.setText(String.valueOf(recipe.getPreparationTime()));
            difficultyComboBox.setValue(recipe.getDifficulty());
            categoryComboBox.setValue(recipe.getCategory());
            instructionsTextArea.setText(String.join("\n", recipe.getInstructionsList()));

            // Change the "Add" button to "Save"
            addButton.setText("Enregistrer");
            addButton.setOnAction(event -> saveModifiedRecipe(recipe)); // Link to save modified recipe
        }
    }

    @FXML
    private void saveModifiedRecipe(Recipe recipe) {
        try {
            String name = nameField.getText();
            int prepTime = Integer.parseInt(prepTimeField.getText());
            Recipe.Difficulty difficulty = difficultyComboBox.getValue();
            Recipe.Category category = categoryComboBox.getValue();
            String instructions = instructionsTextArea.getText();

            if (name.isEmpty() || difficulty == null || category == null) {
                System.out.println("Veuillez remplir tous les champs !");
                return;
            }

            // Update recipe properties
            recipe.setName(name);
            recipe.setPreparationTime(prepTime);
            recipe.setDifficulty(difficulty);
            recipe.setCategory(category); // Update category
            recipe.setInstructionsList(FXCollections.observableArrayList(instructions.split("\n")));

            // Update recipe in DAO
            recipeDAO.update(recipe);

            // Find the index of the modified recipe in the table
            int index = recipesTable.getItems().indexOf(recipe);

            // Update the modified row in the table
            if (index != -1) {
                recipesTable.getItems().set(index, recipe);  // Update the item at the given index
            }

            // Reset the form and restore the "Add" button
            nameField.clear();
            prepTimeField.clear();
            difficultyComboBox.setValue(null);
            categoryComboBox.setValue(null);
            instructionsTextArea.clear();

            addButton.setText("Ajouter");
            addButton.setOnAction(event -> onAddRecipe());  // Restore the "Add" action

            System.out.println("Recette modifiée : " + recipe.getName());
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Le temps de préparation doit être un nombre !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification de la recette : " + e.getMessage());
        }
    }

    @FXML
    private void onAddRecipe() {
        try {
            // Retrieve form values
            String name = nameField.getText();
            int prepTime = Integer.parseInt(prepTimeField.getText());
            Recipe.Difficulty difficulty = difficultyComboBox.getValue();
            Recipe.Category category = categoryComboBox.getValue();
            String instructions = instructionsTextArea.getText();

            if (name.isEmpty() || difficulty == null || category == null) {
                System.out.println("Please fill in all the fields!");
                return;
            }

            // Retrieve selected ingredients from ListView
            ObservableList<Ingredient> selectedIngredients = ingredientListView.getSelectionModel().getSelectedItems();

            // Create a new Recipe object and add ingredients
            Recipe recipe = new Recipe();
            recipe.setName(name);
            recipe.setPreparationTime(prepTime);
            recipe.setDifficulty(difficulty);
            recipe.setCategory(category);
            recipe.setInstructionsList(FXCollections.observableArrayList(instructions.split("\n")));

            // Create a list of IngredientQuantities for the selected ingredients
            List<Recipe.IngredientQuantity> ingredientQuantities = new ArrayList<>();
            for (Ingredient ingredient : selectedIngredients) {
                ingredientQuantities.add(new Recipe.IngredientQuantity(ingredient.getId(), 1)); // Assuming quantity is 1 for now
            }
            recipe.setIngredients(ingredientQuantities);

            // Add the recipe to the DAO
            recipeDAO.add(recipe);

            // Refresh the recipe table
            refreshRecipesTable();

            // Reset the form fields
            nameField.clear();
            prepTimeField.clear();
            difficultyComboBox.setValue(null);
            categoryComboBox.setValue(null);
            instructionsTextArea.clear();
            ingredientListView.getSelectionModel().clearSelection();  // Clear selected ingredients

            System.out.println("Recipe added: " + recipe.getName());
        } catch (NumberFormatException e) {
            System.out.println("Error: Preparation time must be a number!");
        } catch (Exception e) {
            System.out.println("Error when adding recipe: " + e.getMessage());
        }
    }

    private void refreshRecipesTable() {
        recipesTable.setItems(FXCollections.observableArrayList(recipeDAO.getAll()));
    }

    public void gotoHome(MouseEvent e) {
        this.getController().gotoHome();
    }
}
