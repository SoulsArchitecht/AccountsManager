package ru.sshibko.AccountsManager.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition()
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    @Bean
    public OpenAPI accountManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Account Manager API")
                        .description("Serge Account Management System API Documentation")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Shibko Serge")
                                .email("sshibkodev@gmail.com")));

    }
}
