package tech.stl.hcm.candidate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI candidateServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HCM Candidate Service API")
                        .description("API documentation for the HCM Candidate Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HCM Team")
                                .email("support@hcm.com")
                                .url("https://hcm.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://192.168.0.155:30011")
                                .description("Development Server")
                ));
    }
} 