package pharma.Produit_Service.controllers;

import pharma.Produit_Service.entities.Produit;
import pharma.Produit_Service.repositories.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitRepository produitRepository;

    @PatchMapping("/{id}/stock")
    public Produit updateStock(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (updates.containsKey("quantiteStock")) {
            produit.setQuantiteStock((Integer) updates.get("quantiteStock"));
        }

        return produitRepository.save(produit);
    }
}