package dp.grp4.controllers;

import dp.grp4.orders.OrderFirer;
import dp.grp4.orders.OrderListener;
import dp.grp4.orders.OrderType;
import dp.grp4.views.ViewsManager;

import java.util.*;

public class Controller implements OrderFirer {

    private Map<OrderType, Collection<OrderListener>> orderListeners;

    private Controller(){}

    public static void run(ViewsManager viewsManager) {
        Controller controller=new Controller();
        controller.orderListeners = new HashMap<>();
        Arrays.stream(OrderType.values()).forEach(
                t -> controller.orderListeners.put(t, new ArrayList<>())
        );
        viewsManager.setController(controller);
        viewsManager.setSubscription(controller);

        controller.fireOrder(OrderType.SHOW_HOME);
    }

    @Override
    public void subscription(OrderListener orderListener, OrderType... types) {
        for(OrderType t : types)
            orderListeners.get(t).add(orderListener);
    }

    @Override
    public void fireOrder(OrderType orderType) {
        orderListeners.get(orderType).forEach(e1 -> e1.processOrder(orderType));
    }

}
