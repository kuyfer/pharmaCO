package pharma.Livraison_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LivraisonServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LivraisonServiceApplication.class, args);
        System.out.println("\n✅ Livraison Service started on port 8084");
        System.out.println("📦 H2 Console: http://localhost:8084/h2-console");
        System.out.println("🚚 API Base: http://localhost:8084/api/livraisons");
        System.out.println("🏥 Health Check: http://localhost:8084/api/livraisons/health\n");
    }
}