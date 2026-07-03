package com.aegyocounter.server.notification

/**
 * 알림 전송 추상화. 실제 Discord 웹훅 전송(DiscordWebhookSender)과
 * 로컬 로그 대체(LoggingNotificationSender)를 설정으로 스위칭한다.
 */
interface NotificationSender {
    /**
     * @return 전송 성공 여부
     */
    fun send(message: String): Boolean
}
