package com.moviles.lab8.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.lab8.data.CharacterDb
import com.moviles.lab8.data.LocationDb
import com.moviles.lab8.data.UserPreferencesRepository
import com.moviles.lab8.data.local.AppDatabase
import com.moviles.lab8.data.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)
    private val database = AppDatabase.getDatabase(application)

    val userName: StateFlow<String?> = repository.userNameFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun checkIsLoggedIn(): Boolean {
        val name = repository.userNameFlow.firstOrNull()
        return !name.isNullOrBlank()
    }

    fun login(name: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            delay(4000)

            withContext(Dispatchers.IO) {
                val characterDao = database.characterDao()
                val locationDao = database.locationDao()

                val characters = CharacterDb().getAllCharacters().map { it.toEntity() }
                val locations = LocationDb().getAllLocations().map { it.toEntity() }

                characterDao.insertAll(characters)
                locationDao.insertAll(locations)
            }

            repository.saveUserName(name)
            _isLoading.value = false
            onComplete()
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.characterDao().clearAll()
                database.locationDao().clearAll()
            }
            repository.clearUserName()
            onComplete()
        }
    }
}