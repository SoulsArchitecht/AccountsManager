package ru.sshibko.AccountsManager.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
                                .email("sshibkodev@gmail.com")))
                .servers(
                    List.of(new Server().url("https://myacc.shibko-soft.ru")
                            .description("Serge Account Management System API Documentation"))
                );

    }
}
