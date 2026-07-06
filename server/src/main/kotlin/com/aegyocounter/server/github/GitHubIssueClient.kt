package com.aegyocounter.server.github

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.OffsetDateTime

/**
 * GitHub Issues API 클라이언트.
 * token 이 없거나 GitHub 요청이 실패하면 null 또는 빈 목록을 반환한다.
 */
@Component
class GitHubIssueClient(
    private val properties: GitHubProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private val restClient: RestClient = RestClient.builder()
        .baseUrl(properties.baseUrl)
        .build()

    fun createIssue(title: String, body: String, assignees: List<String>): GitHubIssue? =
        withTokenOrNull {
            restClient.post()
                .uri("/repos/{owner}/{repo}/issues", properties.owner, properties.repo)
                .header("Authorization", "Bearer ${properties.token}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .body(CreateIssueRequest(title = title, body = body, assignees = assignees))
                .retrieve()
                .body(GitHubIssue::class.java)
                ?.also { log.info("[GitHub] 이슈 생성 성공: {}", it.htmlUrl) }
        }

    fun getIssue(number: Long): GitHubIssue? =
        withTokenOrNull {
            restClient.get()
                .uri("/repos/{owner}/{repo}/issues/{number}", properties.owner, properties.repo, number)
                .header("Authorization", "Bearer ${properties.token}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .retrieve()
                .body(GitHubIssue::class.java)
        }

    fun getIssues(): List<GitHubIssue> =
        withTokenOrEmptyList {
            restClient.get()
                .uri { builder ->
                    builder
                        .path("/repos/{owner}/{repo}/issues")
                        .queryParam("state", "open")
                        .queryParam("sort", "created")
                        .queryParam("direction", "asc")
                        .build(properties.owner, properties.repo)
                }
                .header("Authorization", "Bearer ${properties.token}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .retrieve()
                .body(Array<GitHubIssue>::class.java)
                ?.toList()
                .orEmpty()
        }

    fun findOldestUnassigned(): GitHubIssue? =
        withTokenOrNull {
            restClient.get()
                .uri { builder ->
                    builder
                        .path("/repos/{owner}/{repo}/issues")
                        .queryParam("state", "open")
                        .queryParam("assignee", "none")
                        .queryParam("sort", "created")
                        .queryParam("direction", "asc")
                        .queryParam("per_page", 100)
                        .build(properties.owner, properties.repo)
                }
                .header("Authorization", "Bearer ${properties.token}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .retrieve()
                .body(Array<GitHubIssue>::class.java)
                ?.firstOrNull { it.isIssue }
        }

    fun assignIssue(number: Long, assignee: String): GitHubIssue? =
        withTokenOrNull {
            restClient.patch()
                .uri("/repos/{owner}/{repo}/issues/{number}", properties.owner, properties.repo, number)
                .header("Authorization", "Bearer ${properties.token}")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .body(AssignIssueRequest(assignees = listOf(assignee)))
                .retrieve()
                .body(GitHubIssue::class.java)
                ?.also { log.info("[GitHub] 이슈 배정 성공: #{} -> {}", it.number, assignee) }
        }

    private fun <T> withTokenOrNull(block: () -> T?): T? {
        if (properties.token.isBlank()) {
            log.warn("[GitHub] GITHUB_TOKEN 미설정")
            return null
        }

        return try {
            block()
        } catch (e: Exception) {
            log.error("[GitHub] 이슈 API 호출 실패", e)
            null
        }
    }

    private fun withTokenOrEmptyList(block: () -> List<GitHubIssue>): List<GitHubIssue> =
        withTokenOrNull(block).orEmpty()

    data class CreateIssueRequest(
        val title: String,
        val body: String,
        val assignees: List<String>,
    )

    data class AssignIssueRequest(
        val assignees: List<String>,
    )

    data class GitHubIssue(
        val number: Long,
        val title: String,
        val body: String? = null,
        val assignees: List<GitHubUser> = emptyList(),
        @get:JsonProperty("html_url") val htmlUrl: String,
        @get:JsonProperty("created_at") val createdAt: OffsetDateTime,
        @get:JsonProperty("pull_request") val pullRequest: Map<String, Any>? = null,
    ) {
        val isIssue: Boolean
            get() = pullRequest == null
    }

    data class GitHubUser(
        val login: String,
    )
}
