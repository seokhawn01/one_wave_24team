package com.gdg.workfit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI(@Value("${app.api.base-url}") String baseUrl) {
        Server server = new Server().url(baseUrl);
        return new OpenAPI().servers(List.of(server));
    }
}
