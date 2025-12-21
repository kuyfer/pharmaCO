package pharma.Livraison_Service.services;

import pharma.Livraison_Service.dtos.GeolocationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeolocationService {

    private final WebClient geolocationWebClient;

    @Value("${app.mock.geolocation.enabled:true}")
    private boolean mockEnabled;

    public Mono<Map<String, Double>> getCoordinates(String address) {
        if (mockEnabled) {
            log.info("Using mock geolocation for address: {}", address);
            return Mono.just(mockGeolocation(address));
        }

        log.info("Fetching real coordinates for address: {}", address);
        return geolocationWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", address)
                        .queryParam("format", "json")
                        .queryParam("limit", "1")
                        .build())
                .retrieve()
                .bodyToMono(GeolocationResponse.class)
                .map(response -> {
                    if (response.getResults() != null && !response.getResults().isEmpty()) {
                        GeolocationResponse.GeoResult result = response.getResults().get(0);
                        return Map.of(
                                "latitude", result.getLat(),
                                "longitude", result.getLon()
                        );
                    }
                    throw new RuntimeException("No geolocation results found");
                })
                .onErrorResume(e -> {
                    log.error("Failed to fetch geolocation: {}", e.getMessage());
                    log.warn("Falling back to mock geolocation");
                    return Mono.just(mockGeolocation(address));
                });
    }

    private Map<String, Double> mockGeolocation(String address) {
        // Mock coordinates for common cities
        if (address.toLowerCase().contains("paris")) {
            return Map.of("latitude", 48.8566, "longitude", 2.3522);
        } else if (address.toLowerCase().contains("lyon")) {
            return Map.of("latitude", 45.7640, "longitude", 4.8357);
        } else if (address.toLowerCase().contains("marseille")) {
            return Map.of("latitude", 43.2965, "longitude", 5.3698);
        } else {
            // Default to Paris coordinates
            return Map.of("latitude", 48.8566, "longitude", 2.3522);
        }
    }
}