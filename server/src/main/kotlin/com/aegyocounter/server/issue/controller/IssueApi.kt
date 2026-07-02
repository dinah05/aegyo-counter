package com.aegyocounter.server.issue.controller

import com.aegyocounter.server.common.response.ApiResponse
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import com.aegyocounter.server.issue.dto.IssueResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Issue", description = "이슈 등록/조회 API")
interface IssueApi {

    @Operation(
        summary = "이슈 등록",
        description = "input(title/content)만 넣으면 바로 등록된다. assignee 를 비우면 codebidoof 로 자동 할당되고, 링크는 하드코딩으로 연결된다.",
    )
    fun create(request: IssueCreateRequestDTO): ResponseEntity<ApiResponse<IssueResponseDTO>>

    @Operation(summary = "이슈 단건 조회", description = "id 로 이슈를 조회한다.")
    fun get(id: Long): ApiResponse<IssueResponseDTO>

    @Operation(summary = "이슈 목록 조회", description = "등록된 이슈 전체를 조회한다.")
    fun getAll(): ApiResponse<List<IssueResponseDTO>>
}
