package pharma.Livraison_Service.exceptions;

public class LivraisonNotFoundException extends RuntimeException {

    public LivraisonNotFoundException(Long id) {
        super("Livraison non trouvée avec ID: " + id);
    }

    public LivraisonNotFoundException(String type, Object value) {
        super("Livraison non trouvée avec " + type + ": " + value);
    }
}