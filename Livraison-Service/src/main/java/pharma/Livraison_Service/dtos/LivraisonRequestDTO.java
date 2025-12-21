package pharma.Livraison_Service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonRequestDTO {

    @NotNull(message = "L'ID de la commande est obligatoire")
    private Long commandeId;

    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;

    @NotBlank(message = "Le nom du client est obligatoire")
    private String clientNom;

    private String clientEmail;

    @NotBlank(message = "L'adresse de livraison est obligatoire")
    private String adresseLivraison;
}