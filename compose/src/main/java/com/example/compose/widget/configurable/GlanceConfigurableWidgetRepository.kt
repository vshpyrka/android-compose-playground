package com.example.compose.widget.configurable

import com.example.compose.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlanceConfigurableWidgetRepository {

    private val _state = MutableStateFlow(R.drawable.ic_parrot)
    val state: StateFlow<Int> = _state

    fun changeImage(imageId: Int) {
        println("AAA GlanceConfigurableWidgetRepository changeImage $imageId")
        _state.value = imageId
    }
}
