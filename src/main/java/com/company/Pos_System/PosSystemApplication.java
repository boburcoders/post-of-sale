package com.company.Pos_System;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PosSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosSystemApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Point of Sale System Docs")
                        .version("2")
                        .description("This document designed for POS System")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .name("Bobur")
                                .email("boburtoshniyozov4y@gmail.com")
                                .url("https://github.com/boburcoders/post-of-sale"))
                        .license(new License()
                                .name("Apache 2")
                                .url("springdoc.org"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("POS System version 2")
                        .url("https://pos-system.net.uz"))
                .servers(List.of(
                        new Server().url("https://pos-system.net.uz").description("Production Server"),
                        new Server().url("http://localhost:8080").description("Test Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
