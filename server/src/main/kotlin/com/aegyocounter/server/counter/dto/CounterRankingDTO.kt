package com.aegyocounter.server.counter.dto

import com.aegyocounter.server.counter.entity.Counter

data class CounterRankingDTO(
    val rank: Int,
    val userKey: String,
    val allBest: Int,
) {
    companion object {
        fun of(rank: Int, counter: Counter): CounterRankingDTO =
            CounterRankingDTO(rank = rank, userKey = counter.userKey, allBest = counter.allBest)
    }
}
