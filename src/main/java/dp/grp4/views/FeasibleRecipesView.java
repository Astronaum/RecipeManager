package dp.grp4.views;

import dp.grp4.controllers.FeasibleRecipesController;
import dp.grp4.helpers.Helper;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FeasibleRecipesView extends InteractiveView  implements InitializableView{

    @FXML
    private TableView<Recipe> feasibleRecipesTable;

    @FXML
    private TableView<Recipe> incompleteRecipesTable;

    @FXML
    private TableColumn<Recipe, String> feasibleNameColumn;

    @FXML
    private TableColumn<Recipe, Integer> feasiblePrepTimeColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Difficulty> feasibleDifficultyColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Category> feasibleCategoryColumn;

    @FXML
    private TableColumn<Recipe, String> incompleteNameColumn;

    @FXML
    private TableColumn<Recipe, Integer> incompletePrepTimeColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Difficulty> incompleteDifficultyColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Category> incompleteCategoryColumn;
    @FXML
    private TableColumn<Recipe, String> incompleteMissingIngredientsColumn;
    private final RecipeDAO recipeDAO = RecipeDAO.getInstance();
    private final ObservableList<Recipe> feasibleRecipesList = FXCollections.observableArrayList();
    private final ObservableList<Recipe> incompleteRecipesList = FXCollections.observableArrayList();

    public static FeasibleRecipesView create(ViewsManager viewsManager) throws IOException {
        FeasibleRecipesController feasibleRecipesController=FeasibleRecipesController.create(viewsManager);
        FeasibleRecipesView feasibleRecipesView = (FeasibleRecipesView) InteractiveView.getView(viewsManager, "feasible_recipes.fxml");
        feasibleRecipesView.setController(feasibleRecipesController);
        return feasibleRecipesView;
    }



    @FXML
    public void initialize() {
        List.of(feasibleRecipesTable,incompleteRecipesTable).forEach(Helper::setTableViewProperties);
        feasibleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        feasiblePrepTimeColumn.setCellValueFactory(new PropertyValueFactory<>("preparationTime"));
        feasibleDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        feasibleCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        incompleteNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        incompletePrepTimeColumn.setCellValueFactory(new PropertyValueFactory<>("preparationTime"));
        incompleteDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        incompleteCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        incompleteMissingIngredientsColumn.setCellValueFactory(cellData -> {
            Recipe recipe = cellData.getValue();
            try {
                Map<Ingredient, Integer> missingIngredients = RecipeDAO.getInstance()
                        .getMissingIngredients(recipe, IngredientDAO.getInstance().getAll());
                String missingIngredientsText = missingIngredients.entrySet().stream()
                        .map(entry -> entry.getKey().getName() + " (" + entry.getValue() + ")")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Aucun ingrédient manquant");
                return new SimpleStringProperty(missingIngredientsText);
            } catch (Exception e) {
                return new SimpleStringProperty("Erreur lors du calcul");
            }
        });
        loadRecipesData();
    }
    @Override
    public Scene getScene(){
        feasibleRecipesTable.refresh();
        incompleteRecipesTable.refresh();
        loadRecipesData();
        return super.getScene();
    }
    private void loadRecipesData() {
            Map<String, List<Recipe>> categorizedRecipes = recipeDAO.suggestRecipes(IngredientDAO.getInstance().getAll());
            List<Recipe> complete = categorizedRecipes.get("Complete");
            List<Recipe> incomplete = categorizedRecipes.get("Incomplete");
            if (complete != null) {
                feasibleRecipesList.setAll(complete);
            }
            if (incomplete != null) {
                incompleteRecipesList.setAll(incomplete);
            }
            feasibleRecipesTable.setItems(feasibleRecipesList);
            incompleteRecipesTable.setItems(incompleteRecipesList);
    }

    public FeasibleRecipesController getController(){
        return (FeasibleRecipesController) super.getController();
    }

    @FXML
    private void gotoRecipes() {
        this.getController().gotoRecipes();
    }

    public void goHome(ActionEvent e) {
        this.getController().gotoHome();
    }

}
