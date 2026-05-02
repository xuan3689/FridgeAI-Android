package com.example.fridgeai_android.data.local

import androidx.room.*
import com.example.fridgeai_android.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_history ORDER BY createdAt ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>
    
    @Query("SELECT * FROM chat_history ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentMessages(limit: Int = 20): List<ChatMessage>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long
    
    @Delete
    suspend fun deleteMessage(message: ChatMessage)
    
    @Query("DELETE FROM chat_history")
    suspend fun deleteAllMessages()
}
