package com.aegyocounter.server.counter.controller

import com.aegyocounter.server.common.response.ApiResponse
import com.aegyocounter.server.common.response.CommonSuccessCode
import com.aegyocounter.server.counter.dto.CounterRankingDTO
import com.aegyocounter.server.counter.dto.CounterResponseDTO
import com.aegyocounter.server.counter.dto.ResetRequestDTO
import com.aegyocounter.server.counter.service.CounterService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/counters")
class CounterController(
    private val counterService: CounterService,
) : CounterApi {

    @GetMapping("/{userKey}")
    override fun get(@PathVariable userKey: String): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.get(userKey))

    @PostMapping("/{userKey}/increment")
    override fun increment(@PathVariable userKey: String): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.increment(userKey))

    @PostMapping("/{userKey}/decrement")
    override fun decrement(@PathVariable userKey: String): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.decrement(userKey))

    @PostMapping("/{userKey}/reset")
    override fun reset(
        @PathVariable userKey: String,
        @Valid @RequestBody request: ResetRequestDTO,
    ): ApiResponse<CounterResponseDTO> =
        ApiResponse.onSuccess(counterService.reset(userKey, request.password), CommonSuccessCode.OK)

    @GetMapping("/ranking")
    override fun ranking(): ApiResponse<List<CounterRankingDTO>> =
        ApiResponse.onSuccess(counterService.ranking())
}
