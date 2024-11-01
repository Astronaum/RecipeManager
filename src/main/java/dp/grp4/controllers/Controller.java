package dp.grp4.controllers;

import dp.grp4.orders.OrderFirer;
import dp.grp4.orders.OrderListener;
import dp.grp4.orders.OrderType;

import java.util.Collection;
import java.util.Map;

public abstract class Controller implements OrderFirer {
    private Map<OrderType, Collection<OrderListener>> orderListeners;

    public Map<OrderType, Collection<OrderListener>> getOrderListeners() {
        return orderListeners;
    }

    public void setOrderListeners(Map<OrderType, Collection<OrderListener>> orderListeners) {
        this.orderListeners = orderListeners;
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
