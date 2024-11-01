package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.*;

public class IngredientsController extends Controller{
    private IngredientsController(){}

    public static IngredientsController create(ViewsManager viewsManager) {
        IngredientsController homeController=new IngredientsController();
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
