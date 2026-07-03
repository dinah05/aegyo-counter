package com.aegyocounter.server.issue.controller

import com.aegyocounter.server.common.response.ApiResponse
import com.aegyocounter.server.common.response.CommonSuccessCode
import com.aegyocounter.server.issue.dto.IssueCreateRequestDTO
import com.aegyocounter.server.issue.dto.IssueResponseDTO
import com.aegyocounter.server.issue.service.IssueService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/issues")
class IssueController(
    private val issueService: IssueService,
) : IssueApi {

    @PostMapping
    override fun create(
        @Valid @RequestBody request: IssueCreateRequestDTO,
    ): ResponseEntity<ApiResponse<IssueResponseDTO>> =
        ResponseEntity
            .status(CommonSuccessCode.CREATED.httpStatus)
            .body(ApiResponse.onSuccess(issueService.create(request), CommonSuccessCode.CREATED))

    @PostMapping("/assign-one")
    override fun assignOneUnassigned(): ApiResponse<IssueResponseDTO> =
        ApiResponse.onSuccess(issueService.assignOneUnassigned())

    @PostMapping("/assign-unassigned")
    override fun assignUnassigned(): ApiResponse<List<IssueResponseDTO>> =
        ApiResponse.onSuccess(issueService.assignUnassigned())

    @PostMapping("/{id}/assign")
    override fun assign(@PathVariable id: Long): ApiResponse<IssueResponseDTO> =
        ApiResponse.onSuccess(issueService.assign(id))

    @GetMapping("/{id}")
    override fun get(@PathVariable id: Long): ApiResponse<IssueResponseDTO> =
        ApiResponse.onSuccess(issueService.get(id))

    @GetMapping
    override fun getAll(): ApiResponse<List<IssueResponseDTO>> =
        ApiResponse.onSuccess(issueService.getAll())
}
