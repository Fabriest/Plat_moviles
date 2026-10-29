package com.moviles.lab8.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.lab8.data.Location
import com.moviles.lab8.data.local.AppDatabase
import com.moviles.lab8.data.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LocationsUiState(
    val isLoading: Boolean = false,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val locationDao = AppDatabase.getDatabase(application).locationDao()

    private val _uiState = MutableStateFlow(LocationsUiState(isLoading = true))
    val uiState: StateFlow<LocationsUiState> = _uiState.asStateFlow()

    init { fetchLocations() }

    fun fetchLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            locationDao.getAllLocations().collect { entities ->
                val locations = entities.map { it.toModel() }
                _uiState.update { it.copy(isLoading = false, data = locations) }
            }
        }
    }

    fun triggerError() {
        _uiState.update { it.copy(hasError = true, isLoading = false) }
    }
}