package com.aegyocounter.app

sealed interface SideEffect {
    data class ShowToast(val message: String): SideEffect
}