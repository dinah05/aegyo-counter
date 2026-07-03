package com.aegyocounter.server.counter.controller

import com.aegyocounter.server.common.response.ApiResponse
import com.aegyocounter.server.counter.dto.CounterResponseDTO
import com.aegyocounter.server.counter.dto.ResetRequestDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

/**
 * Swagger 문서용 인터페이스. 구현체(CounterController)에는 어노테이션을 작성하지 않는다.
 */
@Tag(name = "Counter", description = "전역 애교 카운터 API (모두가 하나의 카운터를 함께 올림)")
interface CounterApi {

    @Operation(summary = "전역 카운터 조회", description = "현재 전역 count/기록을 조회한다.")
    fun get(): ApiResponse<CounterResponseDTO>

    @Operation(
        summary = "카운트 +1",
        description = "전역 카운트를 1 증가시킨다. 임계값(기본 50) 초과 순간 Discord 웹훅으로 알림을 발송한다.",
    )
    fun increment(): ApiResponse<CounterResponseDTO>

    @Operation(summary = "카운트 -1", description = "전역 카운트를 1 감소시킨다. 0 미만으로는 내려가지 않는다.")
    fun decrement(): ApiResponse<CounterResponseDTO>

    @Operation(summary = "카운터 리셋 (관리자)", description = "비밀번호(env)가 일치해야 전역 카운트/오늘 기록을 초기화한다.")
    fun reset(request: ResetRequestDTO): ApiResponse<CounterResponseDTO>
}
