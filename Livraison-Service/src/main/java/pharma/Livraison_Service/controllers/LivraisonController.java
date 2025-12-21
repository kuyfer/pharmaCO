package pharma.Livraison_Service.controllers;

import pharma.Livraison_Service.dtos.LivraisonRequestDTO;
import pharma.Livraison_Service.dtos.LivraisonResponseDTO;
import pharma.Livraison_Service.services.LivraisonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/livraisons")
@RequiredArgsConstructor
public class LivraisonController {

    private final LivraisonService livraisonService;

    @PostMapping
    public Mono<ResponseEntity<LivraisonResponseDTO>> creerLivraison(
            @Valid @RequestBody LivraisonRequestDTO request) {
        return livraisonService.creerLivraison(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivraisonResponseDTO> getLivraisonById(@PathVariable Long id) {
        return ResponseEntity.ok(livraisonService.getLivraisonById(id));
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<LivraisonResponseDTO> getLivraisonByCommandeId(
            @PathVariable Long commandeId) {
        return ResponseEntity.ok(livraisonService.getLivraisonByCommandeId(commandeId));
    }

    @GetMapping("/suivi/{codeSuivi}")
    public ResponseEntity<LivraisonResponseDTO> getLivraisonByTrackingCode(
            @PathVariable String codeSuivi) {
        return ResponseEntity.ok(livraisonService.getLivraisonByTrackingCode(codeSuivi));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<LivraisonResponseDTO> updateStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        return ResponseEntity.ok(livraisonService.updateStatut(id, statut));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Livraison Service is UP on port 8087");
    }
}