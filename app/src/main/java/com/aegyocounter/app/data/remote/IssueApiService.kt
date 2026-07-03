package com.aegyocounter.app.data.remote

import com.aegyocounter.app.data.remote.dto.ApiResponseDto
import com.aegyocounter.app.data.remote.dto.IssueResponseDto
import retrofit2.http.POST

interface IssueApiService {

    // 미할당 이슈 중 가장 오래된 1개를 codebidoof 에게 배정
    @POST("api/v1/issues/assign-one")
    suspend fun assignOne(): ApiResponseDto<IssueResponseDto>
}
