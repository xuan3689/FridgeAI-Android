package com.example.fridgeai_android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Float = 1f,
    val unit: String = "个",
    val addedAt: Long = System.currentTimeMillis(),
    val expireDays: Int = 7,
    val source: String = "camera", // camera, manual, voice
    val imageUrl: String? = null,
    val category: String = "其他" // 蔬菜、肉类、水果、调料等
) {
    fun isExpiringSoon(days: Int = 1): Boolean {
        val expiryTime = addedAt + (expireDays * 24 * 60 * 60 * 1000L)
        val warningTime = expiryTime - (days * 24 * 60 * 60 * 1000L)
        return System.currentTimeMillis() >= warningTime
    }
    
    fun isExpired(): Boolean {
        val expiryTime = addedAt + (expireDays * 24 * 60 * 60 * 1000L)
        return System.currentTimeMillis() >= expiryTime
    }
    
    fun getRemainingDays(): Int {
        val expiryTime = addedAt + (expireDays * 24 * 60 * 60 * 1000L)
        val remaining = (expiryTime - System.currentTimeMillis()) / (24 * 60 * 60 * 1000L)
        return remaining.toInt()
    }
}
