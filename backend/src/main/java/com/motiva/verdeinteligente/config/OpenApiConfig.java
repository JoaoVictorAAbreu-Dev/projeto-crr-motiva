package com.motiva.verdeinteligente.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
            .title("Motiva Verde Inteligente API")
            .description("Operational decision API for highway vegetation management.")
            .version("v1")
            .contact(new Contact().name("TaskFlow Dev")));
    }
}
