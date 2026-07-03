package com.aegyocounter.server.counter.controller

import com.aegyocounter.server.common.response.ApiResponse
import com.aegyocounter.server.common.response.CommonSuccessCode
import com.aegyocounter.server.counter.dto.CounterResponseDTO
import com.aegyocounter.server.counter.dto.ResetRequestDTO
import com.aegyocounter.server.counter.service.CounterService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/counter")
class CounterController(
    private val counterService: CounterService,
) : CounterApi {

    @GetMapping
    override fun get(): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.get())

    @PostMapping("/increment")
    override fun increment(): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.increment())

    @PostMapping("/decrement")
    override fun decrement(): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.decrement())

    @PostMapping("/reset")
    override fun reset(
        @Valid @RequestBody request: ResetRequestDTO,
    ): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.reset(request.password), CommonSuccessCode.OK)
}
