package com.example.fridgeai_android.data.repository

import com.example.fridgeai_android.data.local.IngredientDao
import com.example.fridgeai_android.data.model.Ingredient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientRepository @Inject constructor(
    private val ingredientDao: IngredientDao
) {
    fun getAllIngredients(): Flow<List<Ingredient>> = ingredientDao.getAllIngredients()
    
    fun getIngredientsByCategory(category: String): Flow<List<Ingredient>> = 
        ingredientDao.getIngredientsByCategory(category)
    
    suspend fun getIngredientById(id: Int): Ingredient? = ingredientDao.getIngredientById(id)
    
    suspend fun addIngredient(ingredient: Ingredient): Long = ingredientDao.insertIngredient(ingredient)
    
    suspend fun updateIngredient(ingredient: Ingredient) = ingredientDao.updateIngredient(ingredient)
    
    suspend fun deleteIngredient(ingredient: Ingredient) = ingredientDao.deleteIngredient(ingredient)
    
    suspend fun deleteIngredientById(id: Int) = ingredientDao.deleteIngredientById(id)
    
    fun getExpiringSoonIngredients(days: Int = 1): Flow<List<Ingredient>> {
        val currentTime = System.currentTimeMillis()
        val warningTime = days * 24 * 60 * 60 * 1000L
        return ingredientDao.getExpiringSoonIngredients(currentTime, warningTime)
    }
}
