package com.aegyocounter.server.issue.repository

import com.aegyocounter.server.issue.entity.Issue
import org.springframework.data.jpa.repository.JpaRepository

interface IssueRepository : JpaRepository<Issue, Long>
