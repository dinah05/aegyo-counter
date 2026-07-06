package com.aegyocounter.server.issue.service

import com.aegyocounter.server.github.GitHubIssueClient
import com.aegyocounter.server.issue.IssueConstants
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime

class IssueServiceTest {

    private val gitHubIssueClient = mock<GitHubIssueClient>()
    private val service = IssueService(gitHubIssueClient)

    @Test
    fun `assignee가 없으면 GitHub 미할당 상태로 등록한다`() {
        whenever(
            gitHubIssueClient.createIssue(
                title = "버그",
                body = "리셋이 안돼요",
                assignees = emptyList(),
            ),
        ).doReturn(githubIssue(1, assignees = emptyList()))

        val result = service.create(IssueCreateRequestDTO(title = "버그", content = "리셋이 안돼요", assignee = null))

        assertThat(result.assignee).isNull()
        assertThat(result.link).isEqualTo("https://github.com/LinkYou-2025/LinkU_Android/issues/1")
    }

    @Test
    fun `assignee가 있으면 그대로 사용한다`() {
        whenever(
            gitHubIssueClient.createIssue(
                title = "제안",
                body = "기능 추가",
                assignees = listOf("someone"),
            ),
        ).doReturn(githubIssue(2, assignees = listOf("someone")))

        val result = service.create(IssueCreateRequestDTO(title = "제안", content = "기능 추가", assignee = "someone"))

        assertThat(result.assignee).isEqualTo("someone")
    }

    @Test
    fun `GitHub 미할당 이슈를 codebidoof에게 일괄 배정한다`() {
        val unassigned = listOf(
            githubIssue(1, assignees = emptyList()),
            githubIssue(2, assignees = emptyList()),
        )
        whenever(gitHubIssueClient.getIssues()).doReturn(unassigned)
        whenever(gitHubIssueClient.assignIssue(1, IssueConstants.DEFAULT_ASSIGNEE))
            .doReturn(githubIssue(1, assignees = listOf(IssueConstants.DEFAULT_ASSIGNEE)))
        whenever(gitHubIssueClient.assignIssue(2, IssueConstants.DEFAULT_ASSIGNEE))
            .doReturn(githubIssue(2, assignees = listOf(IssueConstants.DEFAULT_ASSIGNEE)))

        val result = service.assignUnassigned()

        assertThat(result).hasSize(2)
        assertThat(result).allMatch { it.assignee == IssueConstants.DEFAULT_ASSIGNEE }
    }

    @Test
    fun `GitHub 미할당 이슈 1개만 codebidoof에게 배정한다`() {
        val oldest = githubIssue(1, assignees = emptyList())
        whenever(gitHubIssueClient.findOldestUnassigned()).doReturn(oldest)
        whenever(gitHubIssueClient.assignIssue(1, IssueConstants.DEFAULT_ASSIGNEE))
            .doReturn(githubIssue(1, assignees = listOf(IssueConstants.DEFAULT_ASSIGNEE)))

        val result = service.assignOneUnassigned()

        assertThat(result?.assignee).isEqualTo(IssueConstants.DEFAULT_ASSIGNEE)
    }

    @Test
    fun `GitHub 미할당 이슈가 없으면 null을 반환한다`() {
        whenever(gitHubIssueClient.findOldestUnassigned()).doReturn(null)

        val result = service.assignOneUnassigned()

        assertThat(result).isNull()
    }

    private fun githubIssue(
        number: Long,
        assignees: List<String>,
    ): GitHubIssueClient.GitHubIssue =
        GitHubIssueClient.GitHubIssue(
            number = number,
            title = "이슈 $number",
            body = "내용 $number",
            assignees = assignees.map { GitHubIssueClient.GitHubUser(it) },
            htmlUrl = "https://github.com/LinkYou-2025/LinkU_Android/issues/$number",
            createdAt = OffsetDateTime.parse("2026-07-04T00:00:00Z"),
        )
}
