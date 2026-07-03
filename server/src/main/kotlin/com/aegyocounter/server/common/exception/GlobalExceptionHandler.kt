package com.aegyocounter.server.common.exception

import com.aegyocounter.server.common.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ApiResponse<Nothing>> {
        val errorCode = e.errorCode
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ApiResponse.onFailure(errorCode))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, String?>>> {
        val fieldErrors = e.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return ResponseEntity
            .status(CommonErrorCode.INVALID_INPUT.httpStatus)
            .body(ApiResponse.onFailure(CommonErrorCode.INVALID_INPUT, fieldErrors))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(e: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unhandled exception", e)
        return ResponseEntity
            .status(CommonErrorCode.INTERNAL_ERROR.httpStatus)
            .body(ApiResponse.onFailure(CommonErrorCode.INTERNAL_ERROR))
    }
}
