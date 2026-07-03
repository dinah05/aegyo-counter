package com.aegyocounter.server.common.exception

import org.springframework.http.HttpStatus

/**
 * 에러 코드 추상화. 도메인별 *ErrorCode enum이 구현한다.
 * 형식: {도메인}{HTTP상태코드}{순번} (api-response.md 4. 에러 코드 규칙)
 */
interface BaseErrorCode {
    val httpStatus: HttpStatus
    val code: String
    val message: String
}
