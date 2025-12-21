package pharma.Commande_Service.dtos;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRequestDTO {

    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    @NotBlank(message = "Le nom du client est obligatoire")
    private String clientNom;

    @Email(message = "L'email doit être valide")
    private String clientEmail;

    @NotBlank(message = "L'adresse de livraison est obligatoire")
    private String adresseLivraison;
}