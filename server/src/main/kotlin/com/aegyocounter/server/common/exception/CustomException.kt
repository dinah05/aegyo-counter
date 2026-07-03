package com.aegyocounter.server.common.exception

/**
 * 도메인 예외. 도메인 *ErrorCode를 담아 전역 핸들러로 전파한다.
 * (coding-convention.md 8. 예외 처리)
 */
class CustomException(
    val errorCode: BaseErrorCode,
) : RuntimeException(errorCode.message)
