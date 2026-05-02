package com.example.fridgeai_android.data.repository

import com.example.fridgeai_android.data.local.ChatDao
import com.example.fridgeai_android.data.model.ChatMessage
import com.example.fridgeai_android.data.remote.ChatRequest
import com.example.fridgeai_android.data.remote.FridgeApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val apiService: FridgeApiService
) {
    fun getAllMessages(): Flow<List<ChatMessage>> = chatDao.getAllMessages()
    
    suspend fun getRecentMessages(limit: Int = 20): List<ChatMessage> = 
        chatDao.getRecentMessages(limit)
    
    suspend fun addMessage(message: ChatMessage): Long = chatDao.insertMessage(message)
    
    suspend fun deleteMessage(message: ChatMessage) = chatDao.deleteMessage(message)
    
    suspend fun clearHistory() = chatDao.deleteAllMessages()
    
    suspend fun sendMessage(message: String, ingredients: List<String>? = null): Result<String> {
        return try {
            val context = getRecentMessages(10).map {
                mapOf("role" to it.role, "content" to it.content)
            }
            
            val response = apiService.chat(
                ChatRequest(
                    message = message,
                    context = context,
                    ingredients = ingredients
                )
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.reply)
            } else {
                Result.failure(Exception("Chat request failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
