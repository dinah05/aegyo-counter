package com.aegyocounter.server.counter.entity

/**
 * 사용자별 애교 카운터. (멀티 유저 — userKey 로 구분)
 * 인메모리 저장이므로 서버 재시작 시 초기화된다.
 * 동시 요청 대비로 상태 변경 메서드는 @Synchronized 처리한다.
 */
class Counter(
    val userKey: String,
) {
    var count: Int = 0
        private set

    var todayBest: Int = 0
        private set

    var weekBest: Int = 0
        private set

    var allBest: Int = 0
        private set

    /** +1 하고 기록(best)을 갱신한다. */
    @Synchronized
    fun increase() {
        count += 1
        todayBest = maxOf(todayBest, count)
        weekBest = maxOf(weekBest, count)
        allBest = maxOf(allBest, count)
    }

    /** -1 한다. 0 미만으로는 내려가지 않는다. @return 실제로 감소했는지 여부 */
    @Synchronized
    fun decrease(): Boolean {
        if (count == 0) return false
        count -= 1
        return true
    }

    /** count 와 오늘 기록을 초기화한다. (주간/전체 기록은 유지) */
    @Synchronized
    fun reset() {
        count = 0
        todayBest = 0
    }
}
