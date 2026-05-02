package com.example.fridgeai_android.ui.screens.home

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
class HomeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            ingredientRepository.getAllIngredients().collect { ingredients ->
                val expiringCount = ingredients.count { it.isExpiringSoon(2) }
                val categoryStats = ingredients.groupBy { it.category }
                    .mapValues { it.value.size }
                
                _uiState.value = _uiState.value.copy(
                    totalIngredients = ingredients.size,
                    expiringCount = expiringCount,
                    recentIngredients = ingredients.take(5),
                    categoryStats = categoryStats,
                    isLoading = false
                )
            }
        }
    }
    
    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadData()
    }
}

data class HomeUiState(
    val totalIngredients: Int = 0,
    val expiringCount: Int = 0,
    val recentIngredients: List<Ingredient> = emptyList(),
    val categoryStats: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = true
)
