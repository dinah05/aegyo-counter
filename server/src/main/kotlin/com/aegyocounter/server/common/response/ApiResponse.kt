package com.aegyocounter.server.common.response

import com.aegyocounter.server.common.exception.BaseErrorCode
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * 공통 응답 래퍼. (api-response.md 2. 공통 응답 포맷)
 */
@JsonPropertyOrder("isSuccess", "code", "message", "result")
data class ApiResponse<T>(
    // Jackson이 Kotlin Boolean `isSuccess` 를 `success` 로 직렬화하지 않도록 명시
    @get:JsonProperty("isSuccess")
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?,
) {
    companion object {
        fun <T> onSuccess(result: T?, successCode: BaseSuccessCode): ApiResponse<T> =
            ApiResponse(
                isSuccess = true,
                code = successCode.code,
                message = successCode.message,
                result = result,
            )

        fun <T> onSuccess(result: T?): ApiResponse<T> =
            onSuccess(result, CommonSuccessCode.OK)

        fun <T> onFailure(errorCode: BaseErrorCode, result: T? = null): ApiResponse<T> =
            ApiResponse(
                isSuccess = false,
                code = errorCode.code,
                message = errorCode.message,
                result = result,
            )
    }
}
