package com.example.fridgeai_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fridgeai_android.data.model.Ingredient
import com.example.fridgeai_android.data.model.ChatMessage

@Database(
    entities = [Ingredient::class, ChatMessage::class],
    version = 1,
    exportSchema = false
)
abstract class FridgeDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun chatDao(): ChatDao
}
