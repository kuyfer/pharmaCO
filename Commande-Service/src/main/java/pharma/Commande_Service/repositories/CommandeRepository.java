package pharma.Commande_Service.repositories;

import pharma.Commande_Service.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByClientNomContainingIgnoreCase(String clientNom);

    List<Commande> findByStatut(String statut);

    List<Commande> findByProduitId(Long produitId);

    List<Commande> findByClientEmail(String clientEmail);
}