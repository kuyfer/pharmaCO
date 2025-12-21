package pharma.Commande_Service.exceptions;

public class StockInsuffisantException extends RuntimeException {

    public StockInsuffisantException(String message) {
        super(message);
    }

    public StockInsuffisantException(String produit, Integer stockDisponible, Integer stockDemande) {
        super(String.format("Stock insuffisant pour %s. Disponible: %d, Demandé: %d",
                produit, stockDisponible, stockDemande));
    }
}