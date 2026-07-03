package com.aegyocounter.server.counter.service

import com.aegyocounter.server.common.exception.CustomException
import com.aegyocounter.server.counter.entity.Counter
import com.aegyocounter.server.counter.repository.CounterRepository
import com.aegyocounter.server.notification.NotificationProperties
import com.aegyocounter.server.notification.NotificationSender
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CounterServiceTest {

    private val counterRepository = mock<CounterRepository>()
    private val notificationSender = mock<NotificationSender>()

    private fun service(threshold: Int, resetPassword: String = "pw") =
        CounterService(
            counterRepository = counterRepository,
            notificationSender = notificationSender,
            notificationProperties = NotificationProperties(threshold = threshold),
            resetPassword = resetPassword,
        )

    @Test
    fun `임계값의 배수에 도달하면 Discord 알림을 발송한다`() {
        // given: threshold=1, 새 카운터(count 0)에서 1회 증가하면 1 % 1 == 0 으로 배수 도달
        whenever(counterRepository.findAll()).doReturn(emptyList())
        whenever(counterRepository.save(any<Counter>())).doAnswer { it.arguments[0] as Counter }
        whenever(notificationSender.send(any())).doReturn(true)

        val result = service(threshold = 1).increment()

        assertThat(result.count).isEqualTo(1)
        assertThat(result.notificationSent).isTrue()
        verify(notificationSender, times(1)).send(any())
    }

    @Test
    fun `임계값 미만이면 알림을 발송하지 않는다`() {
        whenever(counterRepository.findAll()).doReturn(listOf(Counter.create()))

        val result = service(threshold = 5).increment()

        assertThat(result.count).isEqualTo(1)
        assertThat(result.notificationSent).isFalse()
        verify(notificationSender, never()).send(any())
    }

    @Test
    fun `리셋 비밀번호가 틀리면 예외를 던진다`() {
        assertThatThrownBy { service(threshold = 50, resetPassword = "correct").reset("wrong") }
            .isInstanceOf(CustomException::class.java)
        verify(counterRepository, never()).findAll()
    }

    @Test
    fun `리셋 비밀번호가 맞으면 카운트를 초기화한다`() {
        val counter = Counter.create().apply { increase(); increase() }
        whenever(counterRepository.findAll()).doReturn(listOf(counter))

        val result = service(threshold = 50, resetPassword = "correct").reset("correct")

        assertThat(result.count).isEqualTo(0)
    }
}
