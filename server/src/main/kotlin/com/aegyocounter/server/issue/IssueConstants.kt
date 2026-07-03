package com.aegyocounter.server.issue

/**
 * 이슈 도메인 고정 상수.
 * - 담당자(assignee)는 codebidoof 로 고정. 요청에 assignee 가 없으면 자동 할당한다.
 * - 이슈 연결 링크는 하드코딩한다.
 */
object IssueConstants {
    const val DEFAULT_ASSIGNEE = "codebidoof"
    const val HARDCODED_LINK = "https://github.com/LinkYou-2025/LinkU_Android/issues"
}
