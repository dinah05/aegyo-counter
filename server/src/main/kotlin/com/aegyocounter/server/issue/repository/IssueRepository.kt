package com.aegyocounter.server.issue.repository

import com.aegyocounter.server.issue.entity.Issue
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * 인메모리 이슈 저장소. 서버 재시작 시 초기화된다.
 */
@Repository
class IssueRepository {

    private val sequence = AtomicLong(0)
    private val store = ConcurrentHashMap<Long, Issue>()

    fun save(title: String, content: String, assignee: String, link: String): Issue {
        val id = sequence.incrementAndGet()
        val issue = Issue(id = id, title = title, content = content, assignee = assignee, link = link)
        store[id] = issue
        return issue
    }

    fun findById(id: Long): Issue? = store[id]

    fun findAll(): List<Issue> = store.values.sortedBy { it.id }
}
