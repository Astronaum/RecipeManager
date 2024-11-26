package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.*;

public class ModalController extends Controller {
    private ModalController(){}

    public static ModalController create(ViewsManager viewsManager) {
        ModalController controller=new ModalController();
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
