package com.aegyocounter.server.issue.dto

import com.aegyocounter.server.issue.entity.Issue
import java.time.LocalDateTime

data class IssueResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val assignee: String,
    val link: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(issue: Issue): IssueResponseDTO =
            IssueResponseDTO(
                id = issue.id,
                title = issue.title,
                content = issue.content,
                assignee = issue.assignee,
                link = issue.link,
                createdAt = issue.createdAt,
            )
    }
}
