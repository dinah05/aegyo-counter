package com.aegyocounter.server.counter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * 전역 애교 카운터. 모든 사용자가 하나의 카운터를 함께 올린다. (row 1개, DB 영속)
 * 상태 변경은 도메인 메서드로만 노출한다.
 */
@Entity
@Table(name = "counter")
class Counter protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    var count: Int = 0
        protected set

    @Column(nullable = false)
    var todayBest: Int = 0
        protected set

    @Column(nullable = false)
    var weekBest: Int = 0
        protected set

    @Column(nullable = false)
    var allBest: Int = 0
        protected set

    /** +1 하고 기록(best)을 갱신한다. */
    fun increase() {
        count += 1
        todayBest = maxOf(todayBest, count)
        weekBest = maxOf(weekBest, count)
        allBest = maxOf(allBest, count)
    }

    /** -1 한다. 0 미만으로는 내려가지 않는다. @return 실제로 감소했는지 여부 */
    fun decrease(): Boolean {
        if (count == 0) return false
        count -= 1
        return true
    }

    /** count 와 오늘 기록을 초기화한다. (주간/전체 기록은 유지) */
    fun reset() {
        count = 0
        todayBest = 0
    }

    companion object {
        fun create(): Counter = Counter()
    }
}
