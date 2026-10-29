package com.moviles.lab8.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.moviles.lab8.data.Location
import com.moviles.lab8.data.local.AppDatabase
import com.moviles.lab8.data.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LocationDetailUiState(
    val isLoading: Boolean = false,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val locationId: Int = checkNotNull(savedStateHandle["locationId"])
    private val locationDao = AppDatabase.getDatabase(application).locationDao()

    private val _uiState = MutableStateFlow(LocationDetailUiState(isLoading = true))
    val uiState: StateFlow<LocationDetailUiState> = _uiState.asStateFlow()

    init { fetchLocationDetail() }

    fun fetchLocationDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            locationDao.getLocationById(locationId).collect { entity ->
                if (entity != null) {
                    _uiState.update { it.copy(isLoading = false, data = entity.toModel()) }
                }
            }
        }
    }

    fun triggerError() {
        _uiState.update { it.copy(hasError = true, isLoading = false) }
    }
}