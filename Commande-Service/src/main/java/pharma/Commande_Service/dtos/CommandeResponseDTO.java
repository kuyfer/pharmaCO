package pharma.Commande_Service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponseDTO {

    private Long id;
    private Long produitId;
    private Integer quantite;
    private String clientNom;
    private String clientEmail;
    private String adresseLivraison;
    private LocalDateTime dateCommande;
    private String statut;
    private Double montantTotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}