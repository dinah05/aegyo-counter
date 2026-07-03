package com.aegyocounter.app.data.remote

import android.util.Log
import com.aegyocounter.app.data.remote.dto.IssueResponseDto

class IssueRemoteRepository(
    private val api: IssueApiService = RetrofitProvider.issueApi,
) {
    // 미할당 이슈 1개 배정. 성공 시 배정된 이슈, 없거나 실패 시 null
    suspend fun assignOne(): IssueResponseDto? =
        runCatching { api.assignOne().result }
            .onFailure { Log.w("IssueRemote", "assignOne 실패: ${it.message}") }
            .getOrNull()
}
