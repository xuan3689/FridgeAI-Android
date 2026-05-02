package com.example.fridgeai_android.di

import android.content.Context
import androidx.room.Room
import com.example.fridgeai_android.data.local.ChatDao
import com.example.fridgeai_android.data.local.FridgeDatabase
import com.example.fridgeai_android.data.local.IngredientDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideFridgeDatabase(@ApplicationContext context: Context): FridgeDatabase {
        return Room.databaseBuilder(
            context,
            FridgeDatabase::class.java,
            "fridge_database"
        ).build()
    }
    
    @Provides
    fun provideIngredientDao(database: FridgeDatabase): IngredientDao {
        return database.ingredientDao()
    }
    
    @Provides
    fun provideChatDao(database: FridgeDatabase): ChatDao {
        return database.chatDao()
    }
}
