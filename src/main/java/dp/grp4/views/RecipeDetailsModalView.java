package dp.grp4.views;

import dp.grp4.controllers.RecipeDetailsModalController;
import dp.grp4.models.dao.IngredientDAO;
import dp.grp4.models.entities.Recipe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDetailsModalView extends ModalView {

    @FXML
    private Label recipeNameLabel;

    @FXML
    private ListView<String> ingredientsListView;

    @FXML
    private TextArea instructionsTextArea;

    public static RecipeDetailsModalView create(ViewsManager viewsManager) throws IOException {
        RecipeDetailsModalView view = (RecipeDetailsModalView) ModalView.create(viewsManager, "recipe_details_modal.fxml");
        RecipeDetailsModalController controller = RecipeDetailsModalController.create(viewsManager);
        view.setController(controller);
        return view;
    }

    public void setRecipe(Recipe recipe) {
        recipeNameLabel.setText(recipe.getName());

        // Charger tous les ingrédients pour faire correspondre ID -> Nom
        Map<Long, String> ingredientMap = new HashMap<>();
        try {
            IngredientDAO.getInstance().getAll().forEach(ingredient ->
                    ingredientMap.put(ingredient.getId(), ingredient.getName()));
        } catch (Exception e) {
            ingredientsListView.setItems(FXCollections.observableArrayList(
                    "Erreur lors du chargement des noms d'ingrédients : " + e.getMessage()
            ));
            return;
        }

        // Convertir les ingrédients en une liste avec le format : "Nom: Quantité"
        ingredientsListView.setItems(FXCollections.observableArrayList(
                recipe.getIngredients().stream()
                        .map(iq -> {
                            String ingredientName = ingredientMap.getOrDefault(iq.getId(), "Ingrédient inconnu");
                            return "• " + ingredientName + ", Quantité: " + iq.getQuantity();
                        })
                        .toList()
        ));

        // Ajouter les instructions dans le TextArea
        instructionsTextArea.setText(String.join("\n", recipe.getInstructionsList()));
    }


    @Override
    public void open() {
        Stage modalStage = this.initStage();
        modalStage.setResizable(false);
        modalStage.setWidth(400);
        modalStage.setHeight(500);
        modalStage.showAndWait();
    }

    @FXML
    private void closeModal() {
        close();
    }
}
