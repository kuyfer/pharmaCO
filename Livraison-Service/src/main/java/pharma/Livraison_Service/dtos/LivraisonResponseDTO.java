package pharma.Livraison_Service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonResponseDTO {

    private Long id;
    private Long commandeId;
    private Long produitId;
    private String clientNom;
    private String clientEmail;
    private String adresseLivraison;
    private String statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateLivraisonEstimee;
    private LocalDateTime dateLivraisonReelle;
    private Double latitude;
    private Double longitude;
    private Integer tempsEstimeMinutes;
    private String codeSuivi;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}