package pharma.Livraison_Service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "livraisons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long commandeId;

    @Column(nullable = false)
    private Long produitId;

    @Column(nullable = false)
    private String clientNom;

    @Column(nullable = false)
    private String clientEmail;

    @Column(nullable = false)
    private String adresseLivraison;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, EN_COURS, LIVREE, ANNULEE

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateLivraisonEstimee;

    private LocalDateTime dateLivraisonReelle;

    private Double latitude;

    private Double longitude;

    private Integer tempsEstimeMinutes;

    private String codeSuivi;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.dateCreation = LocalDateTime.now();
        this.codeSuivi = generateTrackingCode();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private String generateTrackingCode() {
        return "LIV" + System.currentTimeMillis() % 1000000;
    }
}