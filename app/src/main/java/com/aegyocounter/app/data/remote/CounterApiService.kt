package com.aegyocounter.app.data.remote

import com.aegyocounter.app.data.remote.dto.ApiResponseDto
import com.aegyocounter.app.data.remote.dto.CounterResponseDto
import retrofit2.http.GET
import retrofit2.http.POST

interface CounterApiService {

    @GET("api/v1/counter")
    suspend fun get(): ApiResponseDto<CounterResponseDto>

    @POST("api/v1/counter/increment")
    suspend fun increment(): ApiResponseDto<CounterResponseDto>

    @POST("api/v1/counter/decrement")
    suspend fun decrement(): ApiResponseDto<CounterResponseDto>
}
