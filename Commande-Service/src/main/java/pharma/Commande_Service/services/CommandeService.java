package pharma.Commande_Service.services;

import pharma.Commande_Service.dtos.CommandeRequestDTO;
import pharma.Commande_Service.dtos.CommandeResponseDTO;
import pharma.Commande_Service.entities.Commande;
import pharma.Commande_Service.exceptions.StockInsuffisantException;
import pharma.Commande_Service.repositories.CommandeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProduitServiceClient produitServiceClient;
    private final LivraisonServiceClient livraisonServiceClient;

    @Transactional
    public CommandeResponseDTO passerCommande(CommandeRequestDTO request) {
        log.info("Processing order for product ID: {}, quantity: {}, client: {}",
                request.getProduitId(), request.getQuantite(), request.getClientNom());

        // 1. Fetch product from Produit-Service using OpenFeign
        Map<String, Object> produit;
        try {
            produit = (Map<String, Object>) produitServiceClient.getProduitById(request.getProduitId());
            log.info("Product received from produit-service: ID={}, Name={}",
                    request.getProduitId(), produit.get("nom"));
        } catch (Exception e) {
            log.error("Failed to fetch product from produit-service: {}", e.getMessage());
            throw new RuntimeException("Produit service unavailable: " + e.getMessage());
        }

        // 2. Check if product exists
        if (produit == null || produit.isEmpty()) {
            log.error("Product not found for ID: {}", request.getProduitId());
            throw new RuntimeException("Product not found with ID: " + request.getProduitId());
        }

        // 3. Extract product details
        String nomProduit = (String) produit.get("nom");
        Double prix;
        Integer stockDisponible;

        try {
            // Handle different number types (might be Integer, Double, or BigDecimal)
            Object prixObj = produit.get("prix");
            Object stockObj = produit.get("quantiteStock");

            if (prixObj instanceof Number) {
                prix = ((Number) prixObj).doubleValue();
            } else {
                throw new RuntimeException("Invalid price format");
            }

            if (stockObj instanceof Number) {
                stockDisponible = ((Number) stockObj).intValue();
            } else {
                throw new RuntimeException("Invalid stock format");
            }
        } catch (Exception e) {
            log.error("Failed to parse product data: {}", e.getMessage());
            throw new RuntimeException("Invalid product data format received from produit-service");
        }

        // 4. Verify stock availability
        if (stockDisponible < request.getQuantite()) {
            log.error("Insufficient stock for product {}: Available={}, Requested={}",
                    nomProduit, stockDisponible, request.getQuantite());
            throw new StockInsuffisantException(nomProduit, stockDisponible, request.getQuantite());
        }

        // 5. Calculate total amount
        Double montantTotal = prix * request.getQuantite();
        log.info("Calculated total: {} x {} = {}", prix, request.getQuantite(), montantTotal);

        // 6. Create order entity
        Commande commande = new Commande();
        commande.setProduitId(request.getProduitId());
        commande.setQuantite(request.getQuantite());
        commande.setClientNom(request.getClientNom());
        commande.setClientEmail(request.getClientEmail());
        commande.setAdresseLivraison(request.getAdresseLivraison());
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut("CREEE");
        commande.setMontantTotal(montantTotal);

        Commande savedCommande = commandeRepository.save(commande);
        log.info("Order saved with ID: {}", savedCommande.getId());

        // 7. Update stock in Produit-Service
        Map<String, Object> stockUpdate = Map.of(
                "quantiteStock", stockDisponible - request.getQuantite()
        );

        boolean stockUpdated = false;
        try {
            Object updateResult = produitServiceClient.updateProduitStock(
                    request.getProduitId(), stockUpdate
            );
            log.info("Stock updated successfully for product ID: {}. New stock: {}",
                    request.getProduitId(), stockUpdate.get("quantiteStock"));

            stockUpdated = true;
            savedCommande.setStatut("STOCK_UPDATED");

        } catch (Exception e) {
            log.error("Failed to update stock for product ID {}: {}",
                    request.getProduitId(), e.getMessage());

            // Mark the order as failed stock update
            savedCommande.setStatut("STOCK_UPDATE_FAILED");
            commandeRepository.save(savedCommande);

            log.warn("Order {} created but stock update failed. Manual intervention needed.",
                    savedCommande.getId());

            throw new RuntimeException("Order created but failed to update stock. Order ID: " +
                    savedCommande.getId() + ". Please contact support.");
        }

        // 8. Create delivery in Livraison-Service (only if stock was updated successfully)
        if (stockUpdated) {
            try {
                log.info("Creating delivery for order ID: {}", savedCommande.getId());

                Map<String, Object> livraisonRequest = Map.of(
                        "commandeId", savedCommande.getId(),
                        "produitId", savedCommande.getProduitId(),
                        "clientNom", savedCommande.getClientNom(),
                        "clientEmail", savedCommande.getClientEmail(),
                        "adresseLivraison", savedCommande.getAdresseLivraison()
                );

                Object livraisonResponse = livraisonServiceClient.creerLivraison(livraisonRequest);
                log.info("Delivery created successfully for order ID: {}. Response: {}",
                        savedCommande.getId(), livraisonResponse);

                savedCommande.setStatut("COMPLETEE");

            } catch (Exception e) {
                log.error("Failed to create delivery for order ID {}: {}",
                        savedCommande.getId(), e.getMessage());

                // Delivery failure shouldn't fail the order, just mark it
                savedCommande.setStatut("DELIVERY_PENDING");
                log.warn("Order {} completed but delivery creation failed. Delivery can be created manually.",
                        savedCommande.getId());
            }
        }

        commandeRepository.save(savedCommande);

        // 9. Return response DTO
        return mapToResponseDTO(savedCommande);
    }

    public CommandeResponseDTO getCommandeById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", id);
                    return new RuntimeException("Commande non trouvée avec ID: " + id);
                });
        log.info("Order found: ID={}, Status={}", commande.getId(), commande.getStatut());
        return mapToResponseDTO(commande);
    }

    private CommandeResponseDTO mapToResponseDTO(Commande commande) {
        CommandeResponseDTO dto = new CommandeResponseDTO();
        dto.setId(commande.getId());
        dto.setProduitId(commande.getProduitId());
        dto.setQuantite(commande.getQuantite());
        dto.setClientNom(commande.getClientNom());
        dto.setClientEmail(commande.getClientEmail());
        dto.setAdresseLivraison(commande.getAdresseLivraison());
        dto.setDateCommande(commande.getDateCommande());
        dto.setStatut(commande.getStatut());
        dto.setMontantTotal(commande.getMontantTotal());
        dto.setCreatedAt(commande.getCreatedAt());
        dto.setUpdatedAt(commande.getUpdatedAt());
        return dto;
    }
}