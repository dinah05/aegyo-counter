package com.aegyocounter.app

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aegyocounter.app.data.CounterDataStore
import com.aegyocounter.app.data.remote.CounterRemoteRepository
import com.aegyocounter.app.data.remote.IssueRemoteRepository
import com.aegyocounter.app.data.remote.dto.CounterResponseDto
import com.aegyocounter.app.model.Achievement
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStore = CounterDataStore(application)
    private val remote = CounterRemoteRepository()
    private val issueRemote = IssueRemoteRepository()

    private val talks = listOf(
        "호에엥..." to "또 시작이네...",
        "ㅎㅇㅇ..." to "정신력 -1",
        "우..우우..~" to "오늘 몇 번째야...",
        "애교 받아줘잉" to "안 속는다.",
        "냥~" to "귀엽긴 한데...",
        "봐줘~~" to "오늘도 시작이네.",
        "잉..." to "참을 인...",
        "헤헤" to "살려줘..."
    )

    private val achievements = listOf(
        Achievement(
            5,
            "애교 피해자 Lv.1",
            "첫 번째 고비를 버텨냈습니다."
        ),
        Achievement(
            10,
            "멘탈 수련생 Lv.2",
            "조금씩 면역이 생기고 있습니다."
        ),
        Achievement(
            15,
            "참을성 전문가 Lv.3",
            "오늘도 쉽지 않네요."
        ),
        Achievement(
            20,
            "철벽 방어 Lv.3",
            "친구도 포기하지 않습니다."
        ),
        Achievement(
            25,
            "애교 저항군 Lv.4",
            "흔들리지 않는 멘탈."
        ),
        Achievement(
            30,
            "멘탈 수호자 Lv.5",
            "이쯤 되면 프로입니다."
        ),
        Achievement(
            40,
            "애교 면역 Lv.6",
            "웬만한 애교는 통하지 않습니다."
        ),
        Achievement(
            50,
            "인간 방패 Lv.7",
            "존경합니다."
        ),
        Achievement(
            75,
            "전설 직전 Lv.8",
            "거의 끝이 보입니다."
        ),
        Achievement(
            100,
            "👑 전설의 생존자 Lv.9",
            "이걸 100번이나 버텼다고?!"
        )
    )

    private val _state = MutableStateFlow(CountState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SideEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {

            // 전역 카운터 주기적 새로고침
            launch {
                while (true) {
                    remote.get()?.let { syncFromServer(it) }
                    delay(POLL_INTERVAL_MS)
                }
            }

            launch {
                dataStore.count.collect { count ->
                    _state.update {
                        it.copy(number = count)
                    }
                }
            }

            launch {
                dataStore.todayBest.collect { value ->
                    _state.update {
                        it.copy(todayBest = value)
                    }
                }
            }

            launch {
                dataStore.weekBest.collect { value ->
                    _state.update {
                        it.copy(weekBest = value)
                    }
                }
            }

            launch {
                dataStore.allBest.collect { value ->
                    _state.update {
                        it.copy(allBest = value)
                    }
                }
            }
        }
    }

    private suspend fun syncFromServer(dto: CounterResponseDto) {
        dataStore.saveCount(dto.count)
        dataStore.saveTodayBest(dto.todayBest)
        dataStore.saveWeekBest(dto.weekBest)
        dataStore.saveAllBest(dto.allBest)
    }

    fun handleIntent(intent: CounterIntent) {
        when (intent) {
            CounterIntent.Up -> increase()
            CounterIntent.Down -> decrease()
            CounterIntent.Reset -> reset()
            CounterIntent.AssignIssue -> assignOneIssue()
        }
    }

    private fun assignOneIssue() {
        viewModelScope.launch {
            val issue = issueRemote.assignOne()
            val message = if (issue != null) {
                "이슈 #${issue.id} 를 ${issue.assignee}에게 배정했어요"
            } else {
                "배정할 미할당 이슈가 없어요"
            }
            _effect.send(SideEffect.ShowToast(message))
        }
    }

    private fun increase() {
        viewModelScope.launch {

            val currentState = _state.value
            val talk = talks.random()

            val newCount = currentState.number + 1
            val todayBest = maxOf(currentState.todayBest, newCount)
            val weekBest = maxOf(currentState.weekBest, newCount)
            val allBest = maxOf(currentState.allBest, newCount)

            _state.update {
                it.copy(
                    number = newCount,
                    todayBest = todayBest,
                    weekBest = weekBest,
                    allBest = allBest,
                    catMessage = talk.first,
                    girlMessage = talk.second
                )
            }

            dataStore.saveCount(newCount)
            dataStore.saveTodayBest(todayBest)
            dataStore.saveWeekBest(weekBest)
            dataStore.saveAllBest(allBest)

            launch { remote.increment()?.let { syncFromServer(it) } }

            achievements.find { it.count == newCount }?.let { achievement ->

                Log.d("ACH", "Achievement Triggered: ${achievement.count}")

                _state.update {
                    it.copy(
                        showAchievementDialog = true,
                        achievementCount = achievement.count,
                        achievementTitle = achievement.title,
                        achievementDescription = achievement.description
                    )
                }
            }
        }
    }

    private fun decrease() {
        viewModelScope.launch {

            if (_state.value.number == 0) {
                _effect.send(
                    SideEffect.ShowToast("0 이하로는 줄일 수 없어요!")
                )
                return@launch
            }

            val currentState = _state.value
            val talk = talks.random()
            val newCount = currentState.number - 1

            _state.update {
                it.copy(
                    number = newCount,
                    catMessage = talk.first,
                    girlMessage = talk.second
                )
            }

            dataStore.saveCount(newCount)

            launch { remote.decrement()?.let { syncFromServer(it) } }
        }
    }

    // reset은 관리자 전용. 앱 버튼은 서버 최신값으로 새로고침만
    private fun reset() {
        viewModelScope.launch {
            remote.get()?.let { syncFromServer(it) }
        }
    }

    fun dismissAchievementDialog() {
        _state.update {
            it.copy(showAchievementDialog = false)
        }
    }

    private companion object {
        const val POLL_INTERVAL_MS = 3000L
    }
}