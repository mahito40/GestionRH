package com.memoire.gestionrh.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Gestion RH API", version = "1.0", description = "API REST pour la gestion des ressources humaines"))
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {

                final String securitySchemeName = "bearerAuth";

                return new OpenAPI()
                                .info(new Info()
                                                .title("Gestion RH API")
                                                .version("1.0")
                                                .description("Documentation API Gestion RH"))

                                // ── Ajoute le cadenas Authorize ──
                                .addSecurityItem(new SecurityRequirement()
                                                .addList(securitySchemeName))

                                .schemaRequirement(
                                                securitySchemeName,
                                                new SecurityScheme()
                                                                .name(securitySchemeName)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT"));
        }
}
