package pharma.Livraison_Service.services;

import pharma.Livraison_Service.dtos.LivraisonRequestDTO;
import pharma.Livraison_Service.dtos.LivraisonResponseDTO;
import pharma.Livraison_Service.entities.Livraison;
import pharma.Livraison_Service.exceptions.LivraisonNotFoundException;
import pharma.Livraison_Service.repositories.LivraisonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final GeolocationService geolocationService;
    private final DeliveryCalculatorService deliveryCalculator;

    @Transactional
    public Mono<LivraisonResponseDTO> creerLivraison(LivraisonRequestDTO request) {
        log.info("Creating delivery for order ID: {}", request.getCommandeId());

        return geolocationService.getCoordinates(request.getAdresseLivraison())
                .map(coordinates -> {
                    // Create delivery entity
                    Livraison livraison = new Livraison();
                    livraison.setCommandeId(request.getCommandeId());
                    livraison.setProduitId(request.getProduitId());
                    livraison.setClientNom(request.getClientNom());
                    livraison.setClientEmail(request.getClientEmail());
                    livraison.setAdresseLivraison(request.getAdresseLivraison());
                    livraison.setStatut("EN_PREPARATION");
                    livraison.setLatitude(coordinates.get("latitude"));
                    livraison.setLongitude(coordinates.get("longitude"));

                    // Calculate delivery time
                    Integer estimatedMinutes = deliveryCalculator.calculateDeliveryTime(
                            coordinates.get("latitude"),
                            coordinates.get("longitude")
                    );

                    livraison.setTempsEstimeMinutes(estimatedMinutes);
                    livraison.setDateLivraisonEstimee(
                            deliveryCalculator.calculateEstimatedDeliveryDate(estimatedMinutes)
                    );

                    // Save delivery
                    Livraison saved = livraisonRepository.save(livraison);
                    log.info("Delivery created: ID={}, Tracking Code={}",
                            saved.getId(), saved.getCodeSuivi());

                    return mapToResponseDTO(saved);
                });
    }

    @Transactional(readOnly = true)
    public LivraisonResponseDTO getLivraisonById(Long id) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new LivraisonNotFoundException(id));
        return mapToResponseDTO(livraison);
    }

    @Transactional(readOnly = true)
    public LivraisonResponseDTO getLivraisonByCommandeId(Long commandeId) {
        Livraison livraison = livraisonRepository.findByCommandeId(commandeId)
                .orElseThrow(() -> new LivraisonNotFoundException("Commande", commandeId));
        return mapToResponseDTO(livraison);
    }

    @Transactional(readOnly = true)
    public LivraisonResponseDTO getLivraisonByTrackingCode(String codeSuivi) {
        Livraison livraison = livraisonRepository.findByCodeSuivi(codeSuivi)
                .orElseThrow(() -> new LivraisonNotFoundException("Tracking Code", codeSuivi));
        return mapToResponseDTO(livraison);
    }

    @Transactional
    public LivraisonResponseDTO updateStatut(Long id, String nouveauStatut) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new LivraisonNotFoundException(id));

        if ("LIVREE".equals(nouveauStatut)) {
            livraison.setDateLivraisonReelle(LocalDateTime.now());
        }

        livraison.setStatut(nouveauStatut);
        Livraison updated = livraisonRepository.save(livraison);

        log.info("Delivery status updated: ID={}, New Status={}", id, nouveauStatut);
        return mapToResponseDTO(updated);
    }

    private LivraisonResponseDTO mapToResponseDTO(Livraison livraison) {
        LivraisonResponseDTO dto = new LivraisonResponseDTO();
        dto.setId(livraison.getId());
        dto.setCommandeId(livraison.getCommandeId());
        dto.setProduitId(livraison.getProduitId());
        dto.setClientNom(livraison.getClientNom());
        dto.setClientEmail(livraison.getClientEmail());
        dto.setAdresseLivraison(livraison.getAdresseLivraison());
        dto.setStatut(livraison.getStatut());
        dto.setDateCreation(livraison.getDateCreation());
        dto.setDateLivraisonEstimee(livraison.getDateLivraisonEstimee());
        dto.setDateLivraisonReelle(livraison.getDateLivraisonReelle());
        dto.setLatitude(livraison.getLatitude());
        dto.setLongitude(livraison.getLongitude());
        dto.setTempsEstimeMinutes(livraison.getTempsEstimeMinutes());
        dto.setCodeSuivi(livraison.getCodeSuivi());
        dto.setCreatedAt(livraison.getCreatedAt());
        dto.setUpdatedAt(livraison.getUpdatedAt());
        return dto;
    }
}