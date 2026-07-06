package com.aegyocounter.server.issue.dto

import com.aegyocounter.server.github.GitHubIssueClient
import java.time.OffsetDateTime

data class IssueResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val assignee: String?,
    val link: String,
    val createdAt: OffsetDateTime,
) {
    companion object {
        fun of(issue: GitHubIssueClient.GitHubIssue): IssueResponseDTO =
            IssueResponseDTO(
                id = issue.number,
                title = issue.title,
                content = issue.body.orEmpty(),
                assignee = issue.assignees.firstOrNull()?.login,
                link = issue.htmlUrl,
                createdAt = issue.createdAt,
            )
    }
}
