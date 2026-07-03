package com.aegyocounter.server.issue.service

import com.aegyocounter.server.common.exception.CustomException
import com.aegyocounter.server.issue.IssueConstants
import com.aegyocounter.server.issue.IssueErrorCode
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import com.aegyocounter.server.issue.dto.IssueResponseDTO
import com.aegyocounter.server.issue.entity.Issue
import com.aegyocounter.server.issue.repository.IssueRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueService(
    private val issueRepository: IssueRepository,
) {

    @Transactional
    fun create(request: IssueCreateRequestDTO): IssueResponseDTO {
        // assignee 를 비우면 미할당(null) 상태로 등록, 링크는 하드코딩으로 연결
        val assignee = request.assignee?.takeIf { it.isNotBlank() }
        val issue = Issue.create(
            title = request.title,
            content = request.content,
            assignee = assignee,
            link = IssueConstants.HARDCODED_LINK,
        )
        return IssueResponseDTO.of(issueRepository.save(issue))
    }

    /** 미할당 이슈 전부를 codebidoof 에게 배정한다. @return 이번에 배정된 이슈들 */
    @Transactional
    fun assignUnassigned(): List<IssueResponseDTO> {
        val targets = issueRepository.findByAssigneeIsNull()
        targets.forEach { it.assignTo(IssueConstants.DEFAULT_ASSIGNEE) }
        return targets.map { IssueResponseDTO.of(it) }
    }

    /** 미할당 이슈 중 가장 오래된 1개만 codebidoof 에게 배정한다. */
    @Transactional
    fun assignOneUnassigned(): IssueResponseDTO {
        val issue = issueRepository.findFirstByAssigneeIsNullOrderByIdAsc()
            ?: throw CustomException(IssueErrorCode.NO_UNASSIGNED)
        issue.assignTo(IssueConstants.DEFAULT_ASSIGNEE)
        return IssueResponseDTO.of(issue)
    }

    /** 특정 이슈를 codebidoof 에게 배정한다. */
    @Transactional
    fun assign(id: Long): IssueResponseDTO {
        val issue = issueRepository.findByIdOrNull(id)
            ?: throw CustomException(IssueErrorCode.NOT_FOUND)
        issue.assignTo(IssueConstants.DEFAULT_ASSIGNEE)
        return IssueResponseDTO.of(issue)
    }

    @Transactional(readOnly = true)
    fun get(id: Long): IssueResponseDTO {
        val issue = issueRepository.findByIdOrNull(id)
            ?: throw CustomException(IssueErrorCode.NOT_FOUND)
        return IssueResponseDTO.of(issue)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<IssueResponseDTO> =
        issueRepository.findAll().map { IssueResponseDTO.of(it) }
}
