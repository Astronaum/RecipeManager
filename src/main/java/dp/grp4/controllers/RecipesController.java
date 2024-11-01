package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.*;

public class RecipesController extends Controller{
    private RecipesController(){}

    public static RecipesController create(ViewsManager viewsManager) {
        RecipesController homeController=new RecipesController();
        homeController.setOrderListeners(new HashMap<>());
        Arrays.stream(OrderType.values()).forEach(
                t -> homeController.getOrderListeners().put(t, new ArrayList<>())
        );
        viewsManager.setSubscription(homeController);
        return homeController;
    }

    public void gotoHome(){
        this.fireOrder(OrderType.SHOW_HOME);
    }

}
