package com.aegyocounter.server.counter.service

import com.aegyocounter.server.common.exception.CustomException
import com.aegyocounter.server.counter.CounterErrorCode
import com.aegyocounter.server.counter.dto.CounterRankingDTO
import com.aegyocounter.server.counter.dto.CounterResponseDTO
import com.aegyocounter.server.counter.repository.CounterRepository
import com.aegyocounter.server.notification.NotificationProperties
import com.aegyocounter.server.notification.NotificationSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CounterService(
    private val counterRepository: CounterRepository,
    private val notificationSender: NotificationSender,
    private val notificationProperties: NotificationProperties,
    @Value("\${counter.reset-password:}") private val resetPassword: String,
) {

    fun get(userKey: String): CounterResponseDTO {
        val counter = counterRepository.findByUserKey(userKey)
            ?: throw CustomException(CounterErrorCode.NOT_FOUND)
        return CounterResponseDTO.of(counter)
    }

    fun increment(userKey: String): CounterResponseDTO {
        val counter = counterRepository.getOrCreate(userKey)

        val previous = counter.count
        counter.increase()

        // count가 임계값(기본 50)을 "넘어서는 순간" 1회 Discord 웹훅 알림 전송
        val threshold = notificationProperties.threshold
        val crossedThreshold = previous < threshold && counter.count >= threshold
        val notificationSent = if (crossedThreshold) {
            notificationSender.send(buildThresholdMessage(userKey, counter.count))
        } else {
            false
        }

        return CounterResponseDTO.of(counter, notificationSent)
    }

    fun decrement(userKey: String): CounterResponseDTO {
        val counter = counterRepository.findByUserKey(userKey)
            ?: throw CustomException(CounterErrorCode.NOT_FOUND)
        if (!counter.decrease()) {
            throw CustomException(CounterErrorCode.ALREADY_ZERO)
        }
        return CounterResponseDTO.of(counter)
    }

    fun reset(userKey: String, password: String): CounterResponseDTO {
        if (resetPassword.isBlank() || password != resetPassword) {
            throw CustomException(CounterErrorCode.INVALID_RESET_PASSWORD)
        }
        val counter = counterRepository.findByUserKey(userKey)
            ?: throw CustomException(CounterErrorCode.NOT_FOUND)
        counter.reset()
        return CounterResponseDTO.of(counter)
    }

    fun ranking(): List<CounterRankingDTO> =
        counterRepository.findTop10ByAllBestDesc()
            .mapIndexed { index, counter -> CounterRankingDTO.of(index + 1, counter) }

    private fun buildThresholdMessage(userKey: String, count: Int): String =
        ":rotating_light: **애교 감시망**\n" +
            "`$userKey` 님의 애교가 또 감지되었습니다.\n" +
            "현재 ${count}회째 기록 중.\n" +
            "도망가셔도 소용없습니다. 이미 전부 저장됐거든요. :file_folder:"
}
