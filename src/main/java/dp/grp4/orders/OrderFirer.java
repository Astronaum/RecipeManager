package dp.grp4.orders;

public interface OrderFirer {
    /**
     * Allows registering a subscriber for
     * different types of events.
     * @param orderListener : order listener
     * @param types : types
     */
    void subscription(OrderListener orderListener,
                    OrderType... types);
    /**
     * Allows broadcasting an event
     * to the concerned subscribers.
     * @param orderType order type
     */
    void fireOrder(OrderType orderType);
}
