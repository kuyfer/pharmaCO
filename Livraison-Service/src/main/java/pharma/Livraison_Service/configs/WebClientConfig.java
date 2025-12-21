package pharma.Livraison_Service.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${app.geolocation.api.url}")
    private String geolocationApiUrl;

    @Bean
    public WebClient geolocationWebClient() {
        return WebClient.builder()
                .baseUrl(geolocationApiUrl)
                .defaultHeader("User-Agent", "PharmaDelivery/1.0")
                .build();
    }
}