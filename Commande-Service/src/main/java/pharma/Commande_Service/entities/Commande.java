package pharma.Commande_Service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "client_nom", nullable = false)
    private String clientNom;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "adresse_livraison", nullable = false)
    private String adresseLivraison;

    @Column(name = "date_commande")
    private LocalDateTime dateCommande;

    @Column(name = "statut", nullable = false)
    private String statut; // "CREEE", "CONFIRMEE", "LIVREE", "ANNULEE"

    @Column(name = "montant_total")
    private Double montantTotal;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dateCommande == null) {
            dateCommande = LocalDateTime.now();
        }
        if (statut == null) {
            statut = "CREEE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}