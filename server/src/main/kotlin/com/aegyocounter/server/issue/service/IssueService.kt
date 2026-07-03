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
        // assignee 가 없으면 codebidoof 로 고정 할당, 링크는 하드코딩으로 바로 연결
        val assignee = request.assignee?.takeIf { it.isNotBlank() } ?: IssueConstants.DEFAULT_ASSIGNEE
        val issue = Issue.create(
            title = request.title,
            content = request.content,
            assignee = assignee,
            link = IssueConstants.HARDCODED_LINK,
        )
        return IssueResponseDTO.of(issueRepository.save(issue))
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
