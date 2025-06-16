package ar.uba.fi.ingsoft1.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
        .components(new Components()
                .addResponses("401", new ApiResponse().description("Unauthorized - Must log in as apropiate user"))
                .addResponses("403", new ApiResponse().description("Forbidden - need to be administrator to access"))
            ).info(new Info()
            .title("IngSoft Shopper")
            .description("tp de ingsoft del grupo 11")
            .version("v0.0.1"));
    }
}

