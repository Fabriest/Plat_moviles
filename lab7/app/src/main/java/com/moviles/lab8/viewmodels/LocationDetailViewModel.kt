package com.moviles.lab8.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.moviles.lab8.LocationDetailRoute
import com.moviles.lab8.data.Location
import com.moviles.lab8.data.LocationDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(LocationDetailUiState())
    val uiState: StateFlow<LocationDetailUiState> = _uiState.asStateFlow()

    private val locationId = savedStateHandle.toRoute<LocationDetailRoute>().locationId

    init { fetchLocationDetail() }

    fun fetchLocationDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            delay(2000)
            _uiState.update { it.copy(isLoading = false, data = LocationDb().getLocationById(locationId)) }
        }
    }

    fun triggerError() = _uiState.update { it.copy(isLoading = false, hasError = true) }
}