
package pharma.Commande_Service.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "produit-service",
        url = "http://localhost:8080"

)
public interface ProduitServiceClient {

    @GetMapping("/api/produits/{id}")
    Object getProduitById(@PathVariable Long id);

    @PatchMapping("/api/produits/{id}")
    Object updateProduitStock(
            @PathVariable Long id,
            @RequestBody Object stockUpdate
    );
}