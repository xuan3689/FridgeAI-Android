package com.example.fridgeai_android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val role: String, // user, assistant
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
