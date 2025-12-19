package pharma.Produit_Service.configs;


import pharma.Produit_Service.entities.Produit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry cors) {

        // Expose IDs in JSON responses
        config.exposeIdsFor(Produit.class);

        // Configure CORS for development
        cors.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080", "http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);

        // Set base path for API
        config.setBasePath("/api");

        // Configure pagination defaults
        config.setDefaultPageSize(20);
        config.setMaxPageSize(100);

        // Disable DELETE method for Produit resources
        config.getExposureConfiguration()
                .forDomainType(Produit.class)
                .withItemExposure((metadata, httpMethods) ->
                        httpMethods.disable(org.springframework.http.HttpMethod.DELETE)
                )
                .withCollectionExposure((metadata, httpMethods) ->
                        httpMethods.disable(org.springframework.http.HttpMethod.DELETE)
                );
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Configure date/time format
        mapper.findAndRegisterModules();
        return mapper;
    }
}