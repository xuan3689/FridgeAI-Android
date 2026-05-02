package com.example.fridgeai_android.ui.screens.inventory

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
class InventoryViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()
    
    init {
        loadIngredients()
    }
    
    private fun loadIngredients() {
        viewModelScope.launch {
            ingredientRepository.getAllIngredients().collect { ingredients ->
                val categories = ingredients.groupBy { it.category }
                _uiState.value = _uiState.value.copy(
                    allIngredients = ingredients,
                    categorizedIngredients = categories,
                    filteredIngredients = filterIngredients(ingredients),
                    isLoading = false
                )
            }
        }
    }
    
    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        updateFilteredIngredients()
    }
    
    fun setSelectedCategory(category: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        updateFilteredIngredients()
    }
    
    private fun updateFilteredIngredients() {
        val filtered = filterIngredients(_uiState.value.allIngredients)
        _uiState.value = _uiState.value.copy(filteredIngredients = filtered)
    }
    
    private fun filterIngredients(ingredients: List<Ingredient>): List<Ingredient> {
        var result = ingredients
        
        val query = _uiState.value.searchQuery
        if (query.isNotBlank()) {
            result = result.filter { it.name.contains(query, ignoreCase = true) }
        }
        
        val category = _uiState.value.selectedCategory
        if (category != null) {
            result = result.filter { it.category == category }
        }
        
        return result
    }
    
    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientRepository.deleteIngredient(ingredient)
        }
    }
    
    fun updateIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientRepository.updateIngredient(ingredient)
        }
    }
}

data class InventoryUiState(
    val allIngredients: List<Ingredient> = emptyList(),
    val categorizedIngredients: Map<String, List<Ingredient>> = emptyMap(),
    val filteredIngredients: List<Ingredient> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val isLoading: Boolean = true
)
