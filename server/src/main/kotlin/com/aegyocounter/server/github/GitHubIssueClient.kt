package com.aegyocounter.server.github

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

/**
 * GitHub 이슈 생성 클라이언트.
 * token 이 없으면 생성을 스킵하고 null 을 반환한다(우리 DB 저장은 그대로 진행).
 */
@Component
class GitHubIssueClient(
    private val properties: GitHubProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private val restClient: RestClient = RestClient.builder()
        .baseUrl(properties.baseUrl)
        .build()

    /**
     * GitHub 이슈를 생성한다.
     * @return 생성된 이슈의 html_url, 스킵/실패 시 null
     */
    fun createIssue(title: String, body: String, assignees: List<String>): String? {
        if (properties.token.isBlank()) {
            log.info("[GitHub] GITHUB_TOKEN 미설정 → 이슈 생성 스킵")
            return null
        }

        return try {
            val response = restClient.post()
                .uri("/repos/{owner}/{repo}/issues", properties.owner, properties.repo)
                .header("Authorization", "Bearer ${properties.token}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .body(CreateIssueRequest(title = title, body = body, assignees = assignees))
                .retrieve()
                .body(CreateIssueResponse::class.java)

            log.info("[GitHub] 이슈 생성 성공: {}", response?.htmlUrl)
            response?.htmlUrl
        } catch (e: Exception) {
            log.error("[GitHub] 이슈 생성 실패", e)
            null
        }
    }

    data class CreateIssueRequest(
        val title: String,
        val body: String,
        val assignees: List<String>,
    )

    data class CreateIssueResponse(
        @get:JsonProperty("html_url") val htmlUrl: String? = null,
        val number: Int? = null,
    )
}
