package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.*;

public class HomeController extends Controller {
    private HomeController(){}

    public static HomeController create(ViewsManager viewsManager) {
        HomeController homeController=new HomeController();
        homeController.setOrderListeners(new HashMap<>());
        Arrays.stream(OrderType.values()).forEach(
                t -> homeController.getOrderListeners().put(t, new ArrayList<>())
        );
        viewsManager.setSubscription(homeController);
        return homeController;
    }
    public void gotoRecipes(){
        this.fireOrder(OrderType.SHOW_RECIPES);
    }
    public void gotoIngredients(){
        this.fireOrder(OrderType.SHOW_INGREDIENTS);
    }

}
