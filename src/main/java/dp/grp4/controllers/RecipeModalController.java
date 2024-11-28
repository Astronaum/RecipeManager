package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RecipeModalController extends Controller {
    private RecipeModalController() {}

    public static RecipeModalController create(ViewsManager viewsManager) {
        RecipeModalController controller = new RecipeModalController();
        controller.setOrderListeners(new HashMap<>());
        Arrays.stream(OrderType.values()).forEach(
                t -> controller.getOrderListeners().put(t, new ArrayList<>())
        );
        viewsManager.setSubscription(controller);
        return controller;
    }
}
