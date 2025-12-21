package pharma.Commande_Service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import pharma.Commande_Service.services.ProduitServiceClient;

@SpringBootApplication
@EnableFeignClients
public class CommandeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommandeServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner testFeign(ProduitServiceClient produitServiceClient) {
        return args -> {
            System.out.println("\n=== Testing OpenFeign Connection ===");
            System.out.println("Trying to call produit-service at: http://localhost:8083/api/produits/1");

            try {
                Object result = produitServiceClient.getProduitById(1L);
                System.out.println("✅ SUCCESS: OpenFeign is working!");
                System.out.println("Response received from produit-service");
                System.out.println("Response type: " + result.getClass().getName());
                System.out.println("Response: " + result);
            } catch (Exception e) {
                System.out.println("❌ FAILED: OpenFeign connection error");
                System.out.println("Error: " + e.getMessage());
                System.out.println("Full error:");
                e.printStackTrace();
            }
            System.out.println("===================================\n");
        };
    }
}