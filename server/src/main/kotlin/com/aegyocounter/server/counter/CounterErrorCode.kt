package com.aegyocounter.server.counter

import com.aegyocounter.server.common.exception.BaseErrorCode
import org.springframework.http.HttpStatus

enum class CounterErrorCode(
    override val httpStatus: HttpStatus,
    override val code: String,
    override val message: String,
) : BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "COUNTER4001", "카운터를 찾을 수 없습니다."),
    ALREADY_ZERO(HttpStatus.CONFLICT, "COUNTER4091", "0 이하로는 줄일 수 없습니다."),
    INVALID_RESET_PASSWORD(HttpStatus.FORBIDDEN, "COUNTER4031", "리셋 비밀번호가 올바르지 않습니다."),
}
