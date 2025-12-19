package pharma.Produit_Service.repositories;


import pharma.Produit_Service.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
        path = "produits",
        collectionResourceRel = "produits",
        itemResourceRel = "produit"
)
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Search by name (case-insensitive)
    @RestResource(path = "by-nom", rel = "byNom")
    List<Produit> findByNomContainingIgnoreCase(@Param("nom") String nom);

    // Check stock availability
    @RestResource(path = "en-stock", rel = "enStock")
    List<Produit> findByQuantiteStockGreaterThan(@Param("min") Integer minStock);

    // Find low stock items
    @RestResource(path = "stock-faible", rel = "stockFaible")
    List<Produit> findByQuantiteStockLessThanEqual(@Param("seuil") Integer seuil);

    // Price range search
    @RestResource(path = "by-price-range", rel = "byPriceRange")
    List<Produit> findByPrixBetween(
            @Param("min") BigDecimal prixMin,
            @Param("max") BigDecimal prixMax
    );

    // Search by exact name
    @RestResource(path = "find-by-exact-nom", rel = "findByExactNom")
    Optional<Produit> findByNom(@Param("nom") String nom);

    // Complex search in name and description
    @Query("SELECT p FROM Produit p WHERE " +
            "LOWER(p.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    @RestResource(path = "search", rel = "search")
    List<Produit> searchByKeyword(@Param("keyword") String keyword);

    // Custom method to decrement stock (not exposed via REST)
    @RestResource(exported = false)
    @Query("UPDATE Produit p SET p.quantiteStock = p.quantiteStock - :quantite WHERE p.id = :id AND p.quantiteStock >= :quantite")
    int decrementerStock(@Param("id") Long id, @Param("quantite") Integer quantite);

    // Prevent delete via REST API (soft delete can be implemented later)
    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Produit entity);
}