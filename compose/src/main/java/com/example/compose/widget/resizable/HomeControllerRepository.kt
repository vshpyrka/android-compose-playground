package com.example.compose.widget.resizable

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class HomeDevice {
    LivingRoom,
    NurseryTV,
    HallwayLights,
    FrontDoor,
}

data class HomeDeviceStatus(
    val device: HomeDevice,
    val isOn: Boolean,
)

object HomeControllerRepository {

    private val _state = MutableStateFlow<List<HomeDeviceStatus>>(emptyList())
    val state: StateFlow<List<HomeDeviceStatus>> = _state

    private val devices = buildList {
        HomeDevice.entries.forEach {
            add(HomeDeviceStatus(device = it, isOn = false))
        }
    }

    init {
        _state.value = devices
    }

    fun toggleDevice(device: HomeDevice) {
        val data = _state.value.toMutableList()
        val index = data.indexOfFirst { it.device == device }
        val currentState = data[index]
        data[index] = HomeDeviceStatus(device, !currentState.isOn)
        _state.value = data
    }
}
