package com.devcourse.be04daangnmarket.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(title = "당근마켓 클론코딩 서비스",
                description = "당근마켓 api 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public GroupedOpenApi userOpenApi() {
        String[] paths = {"/api/v1/members/**"};

        return GroupedOpenApi.builder()
                .group("유저 API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi postOpenApi() {
        String[] paths = {"/api/v1/posts/**"};

        return GroupedOpenApi.builder()
                .group("게시글 API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi commentOpenApi() {
        String[] paths = {"/api/v1/comments/**"};

        return GroupedOpenApi.builder()
                .group("댓글 API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }
}
