package com.example.fridgeai_android.data.repository

import com.example.fridgeai_android.data.model.Recipe
import com.example.fridgeai_android.data.remote.FridgeApiService
import com.example.fridgeai_android.data.remote.RecipeRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val apiService: FridgeApiService
) {
    suspend fun recommendRecipes(ingredients: List<String>): Result<List<Recipe>> {
        return try {
            val response = apiService.recommendRecipes(
                RecipeRequest(ingredients = ingredients)
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.recipes)
            } else {
                Result.failure(Exception("Recipe recommendation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 本地离线菜谱库（降级方案）
    fun getLocalRecipes(ingredients: List<String>): List<Recipe> {
        return localRecipeDatabase.filter { recipe ->
            recipe.ingredients.any { ingredient ->
                ingredients.any { it.contains(ingredient) || ingredient.contains(it) }
            }
        }.take(5)
    }
    
    private val localRecipeDatabase = listOf(
        Recipe(
            id = "1",
            name = "番茄炒蛋",
            ingredients = listOf("番茄", "鸡蛋", "葱", "盐"),
            steps = listOf(
                "鸡蛋打散，加少许盐",
                "番茄切块，葱切段",
                "热油炒蛋，盛出备用",
                "炒番茄至软烂，加入鸡蛋翻炒均匀"
            ),
            cookingTime = 15,
            difficulty = "简单",
            category = "家常菜"
        ),
        Recipe(
            id = "2",
            name = "青椒肉丝",
            ingredients = listOf("青椒", "猪肉", "姜", "蒜", "酱油"),
            steps = listOf(
                "猪肉切丝，加淀粉腌制",
                "青椒切丝，姜蒜切末",
                "热油炒肉丝至变色",
                "加入青椒翻炒，调味出锅"
            ),
            cookingTime = 20,
            difficulty = "简单",
            category = "家常菜"
        ),
        Recipe(
            id = "3",
            name = "蒜蓉西兰花",
            ingredients = listOf("西兰花", "蒜", "盐", "油"),
            steps = listOf(
                "西兰花切小朵，焯水",
                "蒜切末",
                "热油爆香蒜末",
                "加入西兰花翻炒，调味"
            ),
            cookingTime = 10,
            difficulty = "简单",
            category = "素菜"
        ),
        Recipe(
            id = "4",
            name = "红烧排骨",
            ingredients = listOf("排骨", "姜", "葱", "八角", "酱油", "糖"),
            steps = listOf(
                "排骨焯水去血沫",
                "热油炒糖色，加入排骨",
                "加入调料和水，大火烧开",
                "转小火炖40分钟至软烂"
            ),
            cookingTime = 60,
            difficulty = "中等",
            category = "肉菜"
        ),
        Recipe(
            id = "5",
            name = "凉拌黄瓜",
            ingredients = listOf("黄瓜", "蒜", "醋", "香油", "盐"),
            steps = listOf(
                "黄瓜拍碎切段",
                "蒜切末",
                "加入调料拌匀",
                "冷藏10分钟更入味"
            ),
            cookingTime = 5,
            difficulty = "简单",
            category = "凉菜"
        )
    )
}
