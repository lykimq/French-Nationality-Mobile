package com.lykimq_uyen.french_nationality.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lykimq_uyen.french_nationality.data.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Ready(val questionCount: Int, val categoryCount: Int) : MainUiState
    data class Error(val message: String) : MainUiState
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val counts = withContext(Dispatchers.IO) {
                    val db = AppDatabase.getInstance(application)
                    val questionCount = db.questionDao().getQuestionCount()
                    val categoryCount = db.categoryDao().getCategoryCount()
                    questionCount to categoryCount
                }
                _uiState.value = MainUiState.Ready(counts.first, counts.second)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Database init failed", e)
                _uiState.value = MainUiState.Error(e.message ?: "Database error")
            }
        }
    }
}
