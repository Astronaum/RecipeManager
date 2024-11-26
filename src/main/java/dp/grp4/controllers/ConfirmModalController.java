package dp.grp4.controllers;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.*;

public class ConfirmModalController extends Controller {
    private ConfirmModalController(){}
    public static ConfirmModalController create(ViewsManager viewsManager) {
        ConfirmModalController controller=new ConfirmModalController();
        controller.setOrderListeners(new HashMap<>());
        Arrays.stream(OrderType.values()).forEach(
                t -> controller.getOrderListeners().put(t, new ArrayList<>())
        );
        viewsManager.setSubscription(controller);
        return controller;
    }

}
