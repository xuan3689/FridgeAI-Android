package com.example.fridgeai_android.data.model

data class Recipe(
    val id: String,
    val name: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val imageUrl: String? = null,
    val cookingTime: Int = 30, // 分钟
    val difficulty: String = "简单", // 简单、中等、困难
    val category: String = "家常菜"
)
