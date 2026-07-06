package com.aegyocounter.server.issue.dto

import jakarta.validation.constraints.NotBlank

/**
 * GitHub 이슈 등록 요청. assignee 를 비워두면 GitHub 미할당 상태로 등록된다.
 */
data class IssueCreateRequestDTO(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String,

    /** 비워두면 GitHub 미할당 */
    val assignee: String? = null,
)
