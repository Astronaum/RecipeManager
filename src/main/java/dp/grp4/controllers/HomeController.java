package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.*;

public class HomeController extends Controller {
    private HomeController(){}

    public static HomeController create(ViewsManager viewsManager) {
        HomeController controller=new HomeController();
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
    public void gotoIngredients(){
        this.fireOrder(OrderType.SHOW_INGREDIENTS);
    }

}
