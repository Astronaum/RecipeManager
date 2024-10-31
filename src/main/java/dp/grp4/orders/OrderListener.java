package dp.grp4.orders;

public interface OrderListener {
    /**
     * Allows a subscriber to register with an order firer.
     * @param orderFirer: the relevant order generator
     */
    void setSubscription(OrderFirer orderFirer);
    /**
     * Allows describing the processing based on the received order.
     * @param orderType : order type
     */
    void processOrder(OrderType orderType);
}
