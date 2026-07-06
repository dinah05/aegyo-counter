package com.aegyocounter.server.issue.service

import com.aegyocounter.server.common.exception.CustomException
import com.aegyocounter.server.github.GitHubIssueClient
import com.aegyocounter.server.issue.IssueConstants
import com.aegyocounter.server.issue.IssueErrorCode
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import com.aegyocounter.server.issue.dto.IssueResponseDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueService(
    private val gitHubIssueClient: GitHubIssueClient,
) {

    @Transactional(readOnly = true)
    fun create(request: IssueCreateRequestDTO): IssueResponseDTO {
        val assignee = request.assignee?.takeIf { it.isNotBlank() }

        val issue = gitHubIssueClient.createIssue(
            title = request.title,
            body = request.content,
            assignees = listOfNotNull(assignee),
        ) ?: throw CustomException(IssueErrorCode.GITHUB_REQUEST_FAILED)
        return IssueResponseDTO.of(issue)
    }

    /** 미할당 이슈 전부를 codebidoof 에게 배정한다. @return 이번에 배정된 이슈들 */
    @Transactional(readOnly = true)
    fun assignUnassigned(): List<IssueResponseDTO> {
        val targets = gitHubIssueClient.getIssues()
            .filter { it.isIssue && it.assignees.isEmpty() }
        return targets.mapNotNull { issue ->
            gitHubIssueClient.assignIssue(issue.number, IssueConstants.DEFAULT_ASSIGNEE)
        }.map { IssueResponseDTO.of(it) }
    }

    /** 미할당 이슈 중 가장 오래된 1개만 codebidoof 에게 배정한다. */
    @Transactional(readOnly = true)
    fun assignOneUnassigned(): IssueResponseDTO? {
        val issue = gitHubIssueClient.findOldestUnassigned()
            ?: return null
        val assignedIssue = gitHubIssueClient.assignIssue(issue.number, IssueConstants.DEFAULT_ASSIGNEE)
            ?: throw CustomException(IssueErrorCode.GITHUB_REQUEST_FAILED)
        return IssueResponseDTO.of(assignedIssue)
    }

    /** 특정 이슈를 codebidoof 에게 배정한다. */
    @Transactional(readOnly = true)
    fun assign(id: Long): IssueResponseDTO {
        val issue = gitHubIssueClient.assignIssue(id, IssueConstants.DEFAULT_ASSIGNEE)
            ?: throw CustomException(IssueErrorCode.NOT_FOUND)
        return IssueResponseDTO.of(issue)
    }

    @Transactional(readOnly = true)
    fun get(id: Long): IssueResponseDTO {
        val issue = gitHubIssueClient.getIssue(id)
            ?: throw CustomException(IssueErrorCode.NOT_FOUND)
        return IssueResponseDTO.of(issue)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<IssueResponseDTO> =
        gitHubIssueClient.getIssues()
            .filter { it.isIssue }
            .map { IssueResponseDTO.of(it) }
}
