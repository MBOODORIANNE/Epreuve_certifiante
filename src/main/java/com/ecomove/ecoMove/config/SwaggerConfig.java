package com.ecomove.ecoMove.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration de la documentation Swagger/OpenAPI pour la plateforme EcoMove.
 * <p>
 * Fournit une documentation interactive de l'API REST accessible via
 * <a href="http://localhost:8085/swagger-ui.html">Swagger UI</a>.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configure la documentation OpenAPI avec les métadonnées du projet.
     *
     * @return l'objet OpenAPI configuré
     */
    @Bean
    public OpenAPI ecoMoveOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EcoMove Platform API")
                        .description("API REST de la plateforme B2B de covoiturage domicile-travail EcoMove. " +
                                "Cette API permet de gérer les utilisateurs, les entreprises partenaires, " +
                                "les trajets de covoiturage, les réservations, la messagerie et les rapports " +
                                "d'impact environnemental.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MBONGO BOLLO Jarle")
                                .email("jarle.mbongo@ecomove.com")
                                .url("https://www.ecomove.com"))
                        .license(new License()
                                .name("Propriétaire EcoMove")
                                .url("https://www.ecomove.com/license")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Serveur de développement"),
                        new Server().url("https://api.ecomove.com").description("Serveur de production")
                ));
    }
}
