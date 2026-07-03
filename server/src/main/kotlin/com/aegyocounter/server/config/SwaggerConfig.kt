package com.aegyocounter.server.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI().info(
            Info()
                .title("Aegyo Counter API")
                .description("애교 카운터 백엔드 (멀티 유저 카운터 · 이슈 · 카카오 알림톡)")
                .version("v0.0.1"),
        )
}
