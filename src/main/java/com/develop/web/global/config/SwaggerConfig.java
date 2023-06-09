package com.develop.web.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
     @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Media-Center")
                .version("ALPHA 20230609")
                .description("Media-Center Server API Document");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}