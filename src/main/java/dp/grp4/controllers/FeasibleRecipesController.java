package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FeasibleRecipesController extends Controller  {
    private FeasibleRecipesController(){}

    public static FeasibleRecipesController create(ViewsManager viewsManager) {
        FeasibleRecipesController controller=new FeasibleRecipesController();
        controller.setOrderListeners(new HashMap<>());
        Arrays.stream(OrderType.values()).forEach(
                t -> controller.getOrderListeners().put(t, new ArrayList<>())
        );
        viewsManager.setSubscription(controller);
        return controller;
    }
    public void gotoRecipes(){
        this.fireOrder(OrderType.SHOW_RECIPES);
    }
    public void gotoHome(){
        this.fireOrder(OrderType.SHOW_HOME);
    }

}
