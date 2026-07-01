package com.aegyocounter.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("counter_pref")

class CounterDataStore(
    private val context: Context
) {

    private companion object {
        val COUNT = intPreferencesKey("count")
        val TODAY_BEST = intPreferencesKey("today_best")
        val WEEK_BEST = intPreferencesKey("week_best")
        val ALL_BEST = intPreferencesKey("all_best")

        // 마지막으로 앱을 실행한 날짜
        val LAST_DATE = stringPreferencesKey("last_date")
    }

    val count: Flow<Int> =
        context.dataStore.data.map { preferences ->
            preferences[COUNT] ?: 0
        }

    val todayBest: Flow<Int> =
        context.dataStore.data.map { preferences ->
            preferences[TODAY_BEST] ?: 0
        }

    val weekBest: Flow<Int> =
        context.dataStore.data.map { preferences ->
            preferences[WEEK_BEST] ?: 0
        }

    val allBest: Flow<Int> =
        context.dataStore.data.map { preferences ->
            preferences[ALL_BEST] ?: 0
        }

    val lastDate: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[LAST_DATE] ?: ""
        }

    suspend fun saveCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[COUNT] = value
        }
    }

    suspend fun saveTodayBest(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[TODAY_BEST] = value
        }
    }

    suspend fun saveWeekBest(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[WEEK_BEST] = value
        }
    }

    suspend fun saveAllBest(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[ALL_BEST] = value
        }
    }

    suspend fun saveLastDate(value: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_DATE] = value
        }
    }
}