package com.aegyocounter.app.data.remote

import android.util.Log
import com.aegyocounter.app.data.remote.dto.CounterResponseDto

// best-effort: 실패 시 로그만 남기고 null
class CounterRemoteRepository(
    private val api: CounterApiService = RetrofitProvider.counterApi,
) {
    suspend fun increment(): CounterResponseDto? =
        safeCall("increment") { api.increment().result }

    suspend fun decrement(): CounterResponseDto? =
        safeCall("decrement") { api.decrement().result }

    suspend fun get(): CounterResponseDto? =
        safeCall("get") { api.get().result }

    private inline fun <T> safeCall(tag: String, block: () -> T): T? =
        runCatching { block() }
            .onFailure { Log.w("CounterRemote", "$tag 실패: ${it.message}") }
            .getOrNull()
}
