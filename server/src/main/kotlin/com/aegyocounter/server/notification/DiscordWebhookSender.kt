package com.aegyocounter.server.notification

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

/**
 * Discord 웹훅 실제 전송 구현.
 * discord.webhook.enabled=true 이고 url 이 설정되어 있을 때 활성화된다.
 *
 * Discord 웹훅은 { "content": "...", "username": "..." } JSON 을 POST 하면 채널에 메시지가 올라온다.
 */
@Component
@ConditionalOnProperty(prefix = "discord.webhook", name = ["enabled"], havingValue = "true")
class DiscordWebhookSender(
    private val properties: NotificationProperties,
) : NotificationSender {

    private val log = LoggerFactory.getLogger(javaClass)

    private val restClient: RestClient = RestClient.builder().build()

    override fun send(message: String): Boolean {
        if (properties.url.isBlank()) {
            log.warn("[Discord] 웹훅 URL이 비어 있어 전송을 건너뜁니다.")
            return false
        }

        val payload = DiscordWebhookPayload(content = message, username = properties.username)

        return try {
            restClient.post()
                .uri(properties.url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity()
            log.info("[Discord] 웹훅 전송 성공")
            true
        } catch (e: Exception) {
            log.error("[Discord] 웹훅 전송 중 오류", e)
            false
        }
    }

    data class DiscordWebhookPayload(
        val content: String,
        val username: String,
    )
}
