package com.aegyocounter.server.notification

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Discord 웹훅 알림 설정.
 * enabled=false(로컬 기본값)이면 실제 전송 대신 로그로 대체한다.
 */
@ConfigurationProperties(prefix = "discord.webhook")
data class NotificationProperties(
    val enabled: Boolean = false,
    /** Discord 채널 웹훅 URL */
    val url: String = "",
    /** 메시지에 표시할 봇 이름 */
    val username: String = "애교 카운터",
    /** count가 이 값을 넘어서는 순간 알림 전송 */
    val threshold: Int = 50,
)
