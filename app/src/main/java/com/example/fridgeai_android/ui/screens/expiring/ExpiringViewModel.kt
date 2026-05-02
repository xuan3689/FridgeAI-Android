package com.example.fridgeai_android.ui.screens.expiring

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
class ExpiringViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExpiringUiState())
    val uiState: StateFlow<ExpiringUiState> = _uiState.asStateFlow()
    
    init {
        loadExpiringIngredients()
    }
    
    private fun loadExpiringIngredients() {
        viewModelScope.launch {
            ingredientRepository.getExpiringSoonIngredients(3).collect { ingredients ->
                val expired = ingredients.filter { it.isExpired() }
                val expiringSoon = ingredients.filter { !it.isExpired() && it.isExpiringSoon(3) }
                
                _uiState.value = _uiState.value.copy(
                    expiredIngredients = expired,
                    expiringSoonIngredients = expiringSoon,
                    isLoading = false
                )
            }
        }
    }
    
    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientRepository.deleteIngredient(ingredient)
        }
    }
}

data class ExpiringUiState(
    val expiredIngredients: List<Ingredient> = emptyList(),
    val expiringSoonIngredients: List<Ingredient> = emptyList(),
    val isLoading: Boolean = true
)
