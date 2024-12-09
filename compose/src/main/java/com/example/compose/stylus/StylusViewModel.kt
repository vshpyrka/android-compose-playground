package com.example.compose.stylus

import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StylusViewModel : ViewModel() {

    private var _stylusState = MutableStateFlow(StylusState())
    val stylusState: StateFlow<StylusState> = _stylusState

    val openGlLines = mutableListOf<List<Segment>>()

    private fun requestRendering(stylusState: StylusState) {
        // will update the stylusState which will trigger a Flow
        _stylusState.update {
            return@update stylusState
        }
    }


    private fun createStylusState(motionEvent: MotionEvent): StylusState {
        return StylusState(
            tilt = motionEvent.getAxisValue(MotionEvent.AXIS_TILT),
            pressure = motionEvent.pressure,
            orientation = motionEvent.orientation
        )
    }

    fun updateStylusVisualization(motionEvent: MotionEvent) {
        requestRendering(
            createStylusState(motionEvent)
        )
    }
}

inline fun <T> List<T>.findLastIndex(predicate: (T) -> Boolean): Int {
    val iterator = this.listIterator(size)
    var count = 1
    while (iterator.hasPrevious()) {
        val element = iterator.previous()
        if (predicate(element)) {
            return size - count
        }
        count++
    }
    return -1
}