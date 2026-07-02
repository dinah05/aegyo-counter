package com.aegyocounter.server.issue.entity

import java.time.LocalDateTime

/**
 * 이슈. 인메모리 저장이므로 서버 재시작 시 초기화된다.
 */
class Issue(
    val id: Long,
    val title: String,
    val content: String,
    /** 담당자. 요청에 없으면 codebidoof 로 고정 할당된다. */
    val assignee: String,
    /** 하드코딩된 연결 링크. */
    val link: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
