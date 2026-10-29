package com.moviles.lab8.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.lab8.data.Character
import com.moviles.lab8.data.local.AppDatabase
import com.moviles.lab8.data.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharactersUiState(
    val isLoading: Boolean = false,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val filterFavorites: Boolean = false
)

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val characterDao = AppDatabase.getDatabase(application).characterDao()
    private val _uiState = MutableStateFlow(CharactersUiState(isLoading = true))
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    init { fetchCharacters() }

    fun fetchCharacters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }

            characterDao.getAllCharacters().collect { entities ->
                val characters = entities.map { it.toModel() }
                _uiState.update { it.copy(isLoading = false, data = characters) }
            }
        }
    }

    fun toggleFavorite(id: Int, currentStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            characterDao.updateFavoriteStatus(id, !currentStatus)
        }
    }

    fun toggleFilter() {
        _uiState.update { it.copy(filterFavorites = !it.filterFavorites) }
    }

    fun triggerError() {
        _uiState.update { it.copy(hasError = true, isLoading = false) }
    }
}