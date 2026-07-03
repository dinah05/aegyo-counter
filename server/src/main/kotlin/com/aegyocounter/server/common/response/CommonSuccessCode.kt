package com.aegyocounter.server.common.response

import org.springframework.http.HttpStatus

enum class CommonSuccessCode(
    override val httpStatus: HttpStatus,
    override val code: String,
    override val message: String,
) : BaseSuccessCode {
    OK(HttpStatus.OK, "COMMON200", "요청에 성공하였습니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "생성에 성공하였습니다."),
}
