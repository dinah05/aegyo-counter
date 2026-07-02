package com.aegyocounter.server.issue.service

import com.aegyocounter.server.common.exception.CustomException
import com.aegyocounter.server.issue.IssueConstants
import com.aegyocounter.server.issue.IssueErrorCode
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import com.aegyocounter.server.issue.dto.IssueResponseDTO
import com.aegyocounter.server.issue.repository.IssueRepository
import org.springframework.stereotype.Service

@Service
class IssueService(
    private val issueRepository: IssueRepository,
) {

    fun create(request: IssueCreateRequestDTO): IssueResponseDTO {
        // assignee 가 없으면 codebidoof 로 고정 할당, 링크는 하드코딩으로 바로 연결
        val assignee = request.assignee?.takeIf { it.isNotBlank() } ?: IssueConstants.DEFAULT_ASSIGNEE
        val issue = issueRepository.save(
            title = request.title,
            content = request.content,
            assignee = assignee,
            link = IssueConstants.HARDCODED_LINK,
        )
        return IssueResponseDTO.of(issue)
    }

    fun get(id: Long): IssueResponseDTO {
        val issue = issueRepository.findById(id)
            ?: throw CustomException(IssueErrorCode.NOT_FOUND)
        return IssueResponseDTO.of(issue)
    }

    fun getAll(): List<IssueResponseDTO> =
        issueRepository.findAll().map { IssueResponseDTO.of(it) }
}
