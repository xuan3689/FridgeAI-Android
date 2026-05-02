package com.example.fridgeai_android.data.local

import androidx.room.*
import com.example.fridgeai_android.data.model.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients ORDER BY addedAt DESC")
    fun getAllIngredients(): Flow<List<Ingredient>>
    
    @Query("SELECT * FROM ingredients WHERE id = :id")
    suspend fun getIngredientById(id: Int): Ingredient?
    
    @Query("SELECT * FROM ingredients WHERE category = :category ORDER BY addedAt DESC")
    fun getIngredientsByCategory(category: String): Flow<List<Ingredient>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<Ingredient>)
    
    @Update
    suspend fun updateIngredient(ingredient: Ingredient)
    
    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
    
    @Query("DELETE FROM ingredients WHERE id = :id")
    suspend fun deleteIngredientById(id: Int)
    
    @Query("DELETE FROM ingredients")
    suspend fun deleteAllIngredients()
    
    @Query("SELECT * FROM ingredients WHERE (addedAt + (expireDays * 24 * 60 * 60 * 1000)) - :warningTime <= :currentTime ORDER BY addedAt ASC")
    fun getExpiringSoonIngredients(currentTime: Long, warningTime: Long): Flow<List<Ingredient>>
}
