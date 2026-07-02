package com.aegyocounter.server.issue.service

import com.aegyocounter.server.issue.IssueConstants
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import com.aegyocounter.server.issue.repository.IssueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IssueServiceTest {

    private val service = IssueService(IssueRepository())

    @Test
    fun `assignee가 없으면 codebidoof로 자동 할당하고 링크를 하드코딩으로 연결한다`() {
        val result = service.create(IssueCreateRequestDTO(title = "버그", content = "리셋이 안돼요", assignee = null))

        assertThat(result.assignee).isEqualTo(IssueConstants.DEFAULT_ASSIGNEE)
        assertThat(result.link).isEqualTo(IssueConstants.HARDCODED_LINK)
    }

    @Test
    fun `assignee가 있으면 그대로 사용한다`() {
        val result = service.create(IssueCreateRequestDTO(title = "제안", content = "기능 추가", assignee = "someone"))

        assertThat(result.assignee).isEqualTo("someone")
    }
}
