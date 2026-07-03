package com.aegyocounter.server.issue.controller

import com.aegyocounter.server.common.response.ApiResponse
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import com.aegyocounter.server.issue.dto.IssueResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Issue", description = "이슈 등록/조회/배정 API")
interface IssueApi {

    @Operation(
        summary = "이슈 등록",
        description = "input(title/content)만 넣으면 등록된다. assignee 를 비우면 미할당 상태로 등록되고, 링크는 하드코딩으로 연결된다.",
    )
    fun create(request: IssueCreateRequestDTO): ResponseEntity<ApiResponse<IssueResponseDTO>>

    @Operation(
        summary = "미할당 이슈 1개 배정",
        description = "미할당 이슈 중 가장 오래된 1개를 codebidoof 에게 배정한다. 없으면 404.",
    )
    fun assignOneUnassigned(): ApiResponse<IssueResponseDTO>

    @Operation(
        summary = "미할당 이슈 일괄 배정",
        description = "담당자가 없는(미할당) 이슈 전부를 codebidoof 에게 배정한다.",
    )
    fun assignUnassigned(): ApiResponse<List<IssueResponseDTO>>

    @Operation(summary = "이슈 배정", description = "특정 이슈를 codebidoof 에게 배정한다.")
    fun assign(id: Long): ApiResponse<IssueResponseDTO>

    @Operation(summary = "이슈 단건 조회", description = "id 로 이슈를 조회한다.")
    fun get(id: Long): ApiResponse<IssueResponseDTO>

    @Operation(summary = "이슈 목록 조회", description = "등록된 이슈 전체를 조회한다.")
    fun getAll(): ApiResponse<List<IssueResponseDTO>>
}
