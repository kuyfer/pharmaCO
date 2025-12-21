package pharma.Livraison_Service.dtos;

import lombok.Data;
import java.util.List;

@Data
public class GeolocationResponse {
    private List<GeoResult> results;

    @Data
    public static class GeoResult {
        private Double lat;
        private Double lon;
        private String display_name;
    }
}