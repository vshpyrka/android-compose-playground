package com.example.compose.widget.pinned

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

data class GlanceAppPinnedWidgetState(
    val appWidgetId: Int,
    val userName: String,
    val userIcon: Int,
)

sealed class WidgetState {
    data object Loading : WidgetState()
    data class Complete(val state: GlanceAppPinnedWidgetState) : WidgetState()
}

object GlancePinnedWidgetRepository {

    private val _state = MutableStateFlow<List<GlanceAppPinnedWidgetState>>(emptyList())
    val state: StateFlow<List<GlanceAppPinnedWidgetState>> = _state


    fun getAppWidgetState(appWidgetId: Int): Flow<WidgetState> {
        return flow {
            val value = _state
                .map { it.find { item -> item.appWidgetId == appWidgetId } }
                .filterNotNull()
                .first()
            delay(3000)
            emit(WidgetState.Complete(value))
        }
    }

    fun addWidgetState(state: GlanceAppPinnedWidgetState) {
        val data = _state.value.toMutableList()
        data.add(state)
        _state.value = data
    }
}
