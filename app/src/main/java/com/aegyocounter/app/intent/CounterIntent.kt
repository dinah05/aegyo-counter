package com.aegyocounter.app

sealed interface CounterIntent {
    data object Up: CounterIntent
    data object Down: CounterIntent

    data object Reset : CounterIntent
}