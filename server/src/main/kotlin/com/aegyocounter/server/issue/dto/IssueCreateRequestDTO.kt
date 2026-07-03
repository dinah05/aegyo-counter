package com.aegyocounter.server.issue.dto

import jakarta.validation.constraints.NotBlank

/**
 * 이슈 등록 요청. content(input)만 넣어도 바로 등록되고,
 * assignee 를 비워두면 codebidoof 로 자동 할당된다.
 */
data class IssueCreateRequestDTO(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String,

    /** 비워두면 codebidoof 로 고정 할당 */
    val assignee: String? = null,
)
