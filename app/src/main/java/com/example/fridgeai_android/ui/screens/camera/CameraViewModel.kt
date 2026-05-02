package com.example.fridgeai_android.ui.screens.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fridgeai_android.data.model.Ingredient
import com.example.fridgeai_android.data.repository.IngredientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()
    
    fun onRecognitionResult(name: String, confidence: Float, category: String) {
        _uiState.value = _uiState.value.copy(
            recognizedName = name,
            confidence = confidence,
            category = category,
            showConfirmDialog = true
        )
    }
    
    fun saveIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientRepository.addIngredient(ingredient)
            _uiState.value = _uiState.value.copy(
                showConfirmDialog = false,
                recognizedName = null,
                confidence = 0f,
                category = null
            )
        }
    }
    
    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(
            showConfirmDialog = false,
            recognizedName = null,
            confidence = 0f,
            category = null
        )
    }
    
    fun setProcessing(isProcessing: Boolean) {
        _uiState.value = _uiState.value.copy(isProcessing = isProcessing)
    }
}

data class CameraUiState(
    val recognizedName: String? = null,
    val confidence: Float = 0f,
    val category: String? = null,
    val showConfirmDialog: Boolean = false,
    val isProcessing: Boolean = false
)
