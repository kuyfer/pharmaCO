package pharma.Livraison_Service.repositories;

import pharma.Livraison_Service.entities.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    Optional<Livraison> findByCommandeId(Long commandeId);

    List<Livraison> findByStatut(String statut);

    Optional<Livraison> findByCodeSuivi(String codeSuivi);
}