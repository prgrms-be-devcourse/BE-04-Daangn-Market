package com.devcourse.be04daangnmarket.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "당근마켓 클론코딩 서비스",
                description = "당근마켓 api 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {
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
}
