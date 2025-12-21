package pharma.Commande_Service.controllers;

import pharma.Commande_Service.dtos.CommandeRequestDTO;
import pharma.Commande_Service.dtos.CommandeResponseDTO;
import pharma.Commande_Service.exceptions.StockInsuffisantException;
import pharma.Commande_Service.services.CommandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeResponseDTO> passerCommande(
            @Valid @RequestBody CommandeRequestDTO request) {
        try {
            CommandeResponseDTO response = commandeService.passerCommande(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (StockInsuffisantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // You could create an ErrorDTO here
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> getCommande(@PathVariable Long id) {
        CommandeResponseDTO commande = commandeService.getCommandeById(id);
        return ResponseEntity.ok(commande);
    }
}