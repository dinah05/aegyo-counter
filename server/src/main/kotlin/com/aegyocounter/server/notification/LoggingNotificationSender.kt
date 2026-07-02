package com.aegyocounter.server.notification

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

/**
 * 로컬/테스트용 알림 대체 구현. 실제 전송 대신 로그만 남긴다.
 * discord.webhook.enabled 가 false 이거나 없을 때 활성화된다.
 */
@Component
@ConditionalOnProperty(prefix = "discord.webhook", name = ["enabled"], havingValue = "false", matchIfMissing = true)
class LoggingNotificationSender : NotificationSender {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun send(message: String): Boolean {
        log.info("[Discord-MOCK] 실제 전송 대신 로그 출력:\n{}", message)
        return true
    }
}
