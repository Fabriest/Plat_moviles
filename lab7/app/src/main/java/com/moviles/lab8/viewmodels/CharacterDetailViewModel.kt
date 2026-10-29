package com.moviles.lab8.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
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

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])
    private val characterDao = AppDatabase.getDatabase(application).characterDao()

    private val _uiState = MutableStateFlow(CharacterDetailUiState(isLoading = true))
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init { fetchCharacterDetail() }

    fun fetchCharacterDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }

            characterDao.getCharacterById(characterId).collect { entity ->
                if (entity != null) {
                    _uiState.update { it.copy(isLoading = false, data = entity.toModel()) }
                }
            }
        }
    }

    fun toggleFavorite(currentStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            characterDao.updateFavoriteStatus(characterId, !currentStatus)
        }
    }

    fun triggerError() {
        _uiState.update { it.copy(hasError = true, isLoading = false) }
    }
}