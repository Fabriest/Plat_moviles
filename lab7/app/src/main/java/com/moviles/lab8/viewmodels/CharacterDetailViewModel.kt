package com.moviles.lab8.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.moviles.lab8.CharacterDetailRoute
import com.moviles.lab8.data.Character
import com.moviles.lab8.data.CharacterDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    private val characterId = savedStateHandle.toRoute<CharacterDetailRoute>().characterId

    init { fetchCharacterDetail() }

    fun fetchCharacterDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            delay(2000)
            _uiState.update { it.copy(isLoading = false, data = CharacterDb().getCharacterById(characterId)) }
        }
    }

    fun triggerError() = _uiState.update { it.copy(isLoading = false, hasError = true) }
}