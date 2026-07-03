package com.aegyocounter.server.issue.repository

import com.aegyocounter.server.issue.entity.Issue
import org.springframework.data.jpa.repository.JpaRepository

interface IssueRepository : JpaRepository<Issue, Long> {
    /** 담당자가 없는(미할당) 이슈 목록 */
    fun findByAssigneeIsNull(): List<Issue>
}
