package com.aegyocounter.server.counter.dto

import jakarta.validation.constraints.NotBlank

/**
 * 카운터 리셋 요청. 비밀번호(env: COUNTER_RESET_PASSWORD)가 있어야 리셋 가능.
 */
data class ResetRequestDTO(
    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String,
)
