package com.aegyocounter.app.data.remote.dto

data class IssueResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val assignee: String?,
    val link: String,
    val createdAt: String,
)
