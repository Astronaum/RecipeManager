package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;
import javafx.fxml.FXML;

import java.util.*;

public class RecipesController extends Controller{
    private RecipesController(){}

    public static RecipesController create(ViewsManager viewsManager) {
        RecipesController controller=new RecipesController();
        controller.setOrderListeners(new HashMap<>());
        Arrays.stream(OrderType.values()).forEach(
                t -> controller.getOrderListeners().put(t, new ArrayList<>())
        );
        viewsManager.setSubscription(controller);
        return controller;
    }

    public void gotoHome(){
        this.fireOrder(OrderType.SHOW_HOME);
    }

    public void onViewFeasibleRecipes() {
        this.fireOrder(OrderType.SHOW_FEASIBLE_RECIPES);
    }
    public void onShowFavorites() {
        this.fireOrder(OrderType.SHOW_FAVORITES);
    }

    public void gotoRecipes() {
    }
}
