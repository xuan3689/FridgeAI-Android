package com.example.fridgeai_android.ui.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fridgeai_android.data.model.Recipe
import com.example.fridgeai_android.data.repository.IngredientRepository
import com.example.fridgeai_android.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()
    
    init {
        loadIngredients()
    }
    
    private fun loadIngredients() {
        viewModelScope.launch {
            ingredientRepository.getAllIngredients().collect { ingredients ->
                _uiState.value = _uiState.value.copy(
                    availableIngredients = ingredients.map { it.name }
                )
            }
        }
    }
    
    fun recommendRecipes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val ingredients = _uiState.value.availableIngredients
            
            // 先尝试云端推荐
            val result = recipeRepository.recommendRecipes(ingredients)
            
            val recipes = if (result.isSuccess) {
                result.getOrNull() ?: emptyList()
            } else {
                // 降级到本地菜谱
                recipeRepository.getLocalRecipes(ingredients)
            }
            
            _uiState.value = _uiState.value.copy(
                recommendedRecipes = recipes,
                isLoading = false,
                error = if (recipes.isEmpty()) "暂无推荐菜谱" else null
            )
        }
    }
    
    fun selectRecipe(recipe: Recipe) {
        _uiState.value = _uiState.value.copy(selectedRecipe = recipe)
    }
    
    fun clearSelectedRecipe() {
        _uiState.value = _uiState.value.copy(selectedRecipe = null)
    }
}

data class RecipeUiState(
    val availableIngredients: List<String> = emptyList(),
    val recommendedRecipes: List<Recipe> = emptyList(),
    val selectedRecipe: Recipe? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
