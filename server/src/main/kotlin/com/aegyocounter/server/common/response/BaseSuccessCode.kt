package com.aegyocounter.server.common.response

import org.springframework.http.HttpStatus

/**
 * 성공 코드 추상화. 도메인별 성공 응답이 필요하면 각 도메인 패키지에서 구현한다.
 * (api-response.md 5. 성공 코드 규칙)
 */
interface BaseSuccessCode {
    val httpStatus: HttpStatus
    val code: String
    val message: String
}
