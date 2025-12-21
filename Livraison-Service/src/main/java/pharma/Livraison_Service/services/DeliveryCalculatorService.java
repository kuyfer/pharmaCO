package pharma.Livraison_Service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCalculatorService {

    @Value("${app.delivery.base-time-minutes:30}")
    private int baseTimeMinutes;

    @Value("${app.delivery.per-km-minutes:2}")
    private int perKmMinutes;

    // Pharmacy location (fixed)
    private static final Map<String, Double> PHARMACY_LOCATION = Map.of(
            "latitude", 48.8566, // Paris
            "longitude", 2.3522
    );

    public Integer calculateDeliveryTime(Double lat, Double lon) {
        // Calculate distance using Haversine formula
        double distance = calculateDistance(
                PHARMACY_LOCATION.get("latitude"),
                PHARMACY_LOCATION.get("longitude"),
                lat,
                lon
        );

        // Calculate time (base time + time per km)
        int estimatedMinutes = baseTimeMinutes + (int)(distance * perKmMinutes);

        log.info("Delivery calculation: Distance={}km, Estimated time={}min",
                String.format("%.2f", distance), estimatedMinutes);

        return estimatedMinutes;
    }

    public LocalDateTime calculateEstimatedDeliveryDate(Integer minutes) {
        return LocalDateTime.now().plusMinutes(minutes);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate distance between two points
        double R = 6371; // Earth's radius in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in km
    }
}