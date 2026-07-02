package com.aegyocounter.server.counter.repository

import com.aegyocounter.server.counter.entity.Counter
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

/**
 * 인메모리 카운터 저장소 (userKey -> Counter). 서버 재시작 시 초기화된다.
 */
@Repository
class CounterRepository {

    private val store = ConcurrentHashMap<String, Counter>()

    fun findByUserKey(userKey: String): Counter? = store[userKey]

    /** 없으면 새로 만들어 저장하고 반환한다. */
    fun getOrCreate(userKey: String): Counter =
        store.computeIfAbsent(userKey) { Counter(it) }

    /** 멀티 유저 랭킹: 전체 최고 기록(allBest) 내림차순 상위 10 */
    fun findTop10ByAllBestDesc(): List<Counter> =
        store.values.sortedByDescending { it.allBest }.take(10)
}
