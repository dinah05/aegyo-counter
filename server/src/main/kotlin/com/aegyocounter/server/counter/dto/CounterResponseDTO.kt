package com.aegyocounter.server.counter.dto

import com.aegyocounter.server.counter.entity.Counter

data class CounterResponseDTO(
    val count: Int,
    val todayBest: Int,
    val weekBest: Int,
    val allBest: Int,
    /** 이번 응답에서 임계값을 넘겨 Discord 알림이 발송되었는지 */
    val notificationSent: Boolean,
) {
    companion object {
        fun of(counter: Counter, notificationSent: Boolean = false): CounterResponseDTO =
            CounterResponseDTO(
                count = counter.count,
                todayBest = counter.todayBest,
                weekBest = counter.weekBest,
                allBest = counter.allBest,
                notificationSent = notificationSent,
            )
    }
}
