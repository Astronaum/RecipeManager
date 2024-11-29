package dp.grp4.views;

import dp.grp4.controllers.FavoritesRecipesController;
import dp.grp4.helpers.Helper;
import dp.grp4.models.dao.RecipeDAO;
import dp.grp4.models.entities.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.stream.Collectors;

public class FavoritesRecipesView extends InteractiveView implements InitializableView {

    @FXML
    private TableView<Recipe> favoritesTable;

    @FXML
    private TableColumn<Recipe, String> nameColumn;

    @FXML
    private TableColumn<Recipe, Integer> prepTimeColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Difficulty> difficultyColumn;

    @FXML
    private TableColumn<Recipe, Recipe.Category> categoryColumn;

    private final RecipeDAO recipeDAO = RecipeDAO.getInstance();
    private ObservableList<Recipe> favoriteRecipesList = FXCollections.observableArrayList();

    public static FavoritesRecipesView create(ViewsManager viewsManager) throws IOException {
        FavoritesRecipesController favoritesRecipesController=FavoritesRecipesController.create(viewsManager);
        FavoritesRecipesView favoritesRecipesView = (FavoritesRecipesView) InteractiveView.getView(viewsManager, "favorites_recipes.fxml");
        favoritesRecipesView.setController(favoritesRecipesController);
        return favoritesRecipesView;
    }



    @FXML
    public void initialize() {
        Helper.setTableViewProperties(favoritesTable);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        prepTimeColumn.setCellValueFactory(new PropertyValueFactory<>("preparationTime"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadFavoriteRecipesData();
    }
    @Override
    public Scene getScene(){
        favoritesTable.refresh();
        loadFavoriteRecipesData();
        return super.getScene();
    }

    private void loadFavoriteRecipesData() {
        try {
            favoriteRecipesList.setAll(recipeDAO.getAll().stream()
                    .filter(Recipe::isFavourite)
                    .collect(Collectors.toList()));
            favoritesTable.setItems(favoriteRecipesList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public FavoritesRecipesController getController(){
        return (FavoritesRecipesController) super.getController();
    }

    @FXML
    private void gotoRecipes() {
        this.getController().gotoRecipes();
    }

    public void goHome(ActionEvent e) {
        this.getController().gotoHome();
    }
}
