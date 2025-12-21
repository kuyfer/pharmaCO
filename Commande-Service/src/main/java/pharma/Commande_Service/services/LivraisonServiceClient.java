package pharma.Commande_Service.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "livraison-service", url = "http://localhost:8080")
public interface LivraisonServiceClient {

    @PostMapping("/api/livraisons")
    Object creerLivraison(@RequestBody Object livraisonRequest);
}