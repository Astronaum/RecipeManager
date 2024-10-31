package dp.grp4.controllers;

public interface OrderListener {
    /**
     * Permet à un abonné de s'inscrire au près
     * d'un lanceur d'ordres
     * @param orderFirer : le générateur concerné
     */
    void setSubscription(OrderFirer orderFirer);
    /**
     * Permet de décrire le traitement en fonction
     * de l'ordre reçu
     * @param orderType
     */
    void processOrder(OrderType orderType);
}
