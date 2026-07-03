package com.aegyocounter.server.counter.service

import com.aegyocounter.server.common.exception.CustomException
import com.aegyocounter.server.counter.CounterErrorCode
import com.aegyocounter.server.counter.dto.CounterResponseDTO
import com.aegyocounter.server.counter.entity.Counter
import com.aegyocounter.server.counter.repository.CounterRepository
import com.aegyocounter.server.notification.NotificationProperties
import com.aegyocounter.server.notification.NotificationSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CounterService(
    private val counterRepository: CounterRepository,
    private val notificationSender: NotificationSender,
    private val notificationProperties: NotificationProperties,
    @Value("\${counter.reset-password:}") private val resetPassword: String,
) {

    /** 전역 카운터(row 1개)를 가져오거나 없으면 생성한다. */
    private fun getOrCreate(): Counter =
        counterRepository.findAll().firstOrNull() ?: counterRepository.save(Counter.create())

    @Transactional
    fun get(): CounterResponseDTO = CounterResponseDTO.of(getOrCreate())

    @Transactional
    fun increment(): CounterResponseDTO {
        val counter = getOrCreate()
        counter.increase()

        // count가 임계값(기본 50)의 배수가 될 때마다 Discord 웹훅 알림 전송 (50, 100, 150...)
        val threshold = notificationProperties.threshold
        val reachedMultiple = counter.count > 0 && counter.count % threshold == 0
        val notificationSent = if (reachedMultiple) {
            notificationSender.send(buildThresholdMessage(counter.count))
        } else {
            false
        }

        return CounterResponseDTO.of(counter, notificationSent)
    }

    @Transactional
    fun decrement(): CounterResponseDTO {
        val counter = getOrCreate()
        if (!counter.decrease()) {
            throw CustomException(CounterErrorCode.ALREADY_ZERO)
        }
        return CounterResponseDTO.of(counter)
    }

    /** 관리자 전용. env COUNTER_RESET_PASSWORD 와 일치해야 초기화된다. */
    @Transactional
    fun reset(password: String): CounterResponseDTO {
        if (resetPassword.isBlank() || password != resetPassword) {
            throw CustomException(CounterErrorCode.INVALID_RESET_PASSWORD)
        }
        val counter = getOrCreate()
        counter.reset()
        return CounterResponseDTO.of(counter)
    }

    private fun buildThresholdMessage(count: Int): String =
        ":rotating_light: **애교 감시망**\n" +
            "누군가의 애교가 또 감지되었습니다.\n" +
            "전역 카운트 ${count}회 돌파.\n" +
            "도망가셔도 소용없습니다. 이미 전부 저장됐거든요. :file_folder:"
}
