package com.aegyocounter.server.github

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * GitHub 이슈 연동 설정.
 * token 이 비어 있으면 실제 이슈 생성을 건너뛴다.
 */
@ConfigurationProperties(prefix = "github")
data class GitHubProperties(
    val token: String = "",
    val owner: String = "LinkYou-2025",
    val repo: String = "LinkU_Android",
    val baseUrl: String = "https://api.github.com",
)
