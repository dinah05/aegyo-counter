package com.aegyocounter.server.issue

import com.aegyocounter.server.common.exception.BaseErrorCode
import org.springframework.http.HttpStatus

enum class IssueErrorCode(
    override val httpStatus: HttpStatus,
    override val code: String,
    override val message: String,
) : BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "ISSUE4001", "이슈를 찾을 수 없습니다."),
    GITHUB_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "ISSUE5001", "GitHub 이슈 요청에 실패했습니다."),
}
