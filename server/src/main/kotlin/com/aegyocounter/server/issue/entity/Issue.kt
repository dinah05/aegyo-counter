package com.aegyocounter.server.issue.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "issue")
class Issue protected constructor(
    title: String,
    content: String,
    assignee: String,
    link: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    val title: String = title

    @Column(nullable = false, length = 2000)
    val content: String = content

    /** 담당자. 요청에 없으면 codebidoof 로 고정 할당된다. */
    @Column(nullable = false)
    val assignee: String = assignee

    /** 하드코딩된 연결 링크. */
    @Column(nullable = false)
    val link: String = link

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    companion object {
        fun create(title: String, content: String, assignee: String, link: String): Issue =
            Issue(title = title, content = content, assignee = assignee, link = link)
    }
}
