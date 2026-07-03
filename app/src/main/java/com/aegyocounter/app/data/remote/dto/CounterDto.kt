package com.aegyocounter.app.data.remote.dto

data class ApiResponseDto<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?,
)

data class CounterResponseDto(
    val count: Int,
    val todayBest: Int,
    val weekBest: Int,
    val allBest: Int,
    val notificationSent: Boolean,
)
