package dp.grp4.controllers;

public interface OrderFirer {
    /**
     * Permet d'enregistrer un abonné pour
     * différents types d'événements
     * @param orderListener
     * @param types
     */
    void subscription(OrderListener orderListener,
                    OrderType... types);
    /**
     * Permet de diffuser un événement
     * aux abonnés concernés
     * @param e
     */
    void fireOrder(OrderType e);
}
