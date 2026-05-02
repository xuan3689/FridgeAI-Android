package com.example.fridgeai_android.data.remote

import com.example.fridgeai_android.data.model.Recipe
import retrofit2.Response
import retrofit2.http.*

interface FridgeApiService {
    
    // 食材识别
    @POST("api/vision/recognize")
    suspend fun recognizeIngredient(@Body request: RecognizeRequest): Response<RecognizeResponse>
    
    // 菜谱推荐
    @POST("api/recipe/recommend")
    suspend fun recommendRecipes(@Body request: RecipeRequest): Response<RecipeResponse>
    
    // 语音识别
    @POST("api/asr/recognize")
    suspend fun recognizeSpeech(@Body request: SpeechRequest): Response<SpeechResponse>
    
    // 语音合成
    @POST("api/tts/synthesize")
    suspend fun synthesizeSpeech(@Body request: TTSRequest): Response<TTSResponse>
    
    // 大模型对话
    @POST("api/llm/chat")
    suspend fun chat(@Body request: ChatRequest): Response<ChatResponse>
    
    // 获取库存状态
    @GET("api/inventory/status")
    suspend fun getInventoryStatus(): Response<InventoryStatusResponse>
}

// Request/Response Models
data class RecognizeRequest(
    val imageBase64: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class RecognizeResponse(
    val success: Boolean,
    val ingredient: String?,
    val confidence: Float,
    val category: String?
)

data class RecipeRequest(
    val ingredients: List<String>,
    val preferences: Map<String, String>? = null
)

data class RecipeResponse(
    val success: Boolean,
    val recipes: List<Recipe>
)

data class SpeechRequest(
    val audioBase64: String,
    val format: String = "wav"
)

data class SpeechResponse(
    val success: Boolean,
    val text: String
)

data class TTSRequest(
    val text: String,
    val voice: String = "default"
)

data class TTSResponse(
    val success: Boolean,
    val audioBase64: String
)

data class ChatRequest(
    val message: String,
    val context: List<Map<String, String>>? = null,
    val ingredients: List<String>? = null
)

data class ChatResponse(
    val success: Boolean,
    val reply: String
)

data class InventoryStatusResponse(
    val success: Boolean,
    val totalItems: Int,
    val expiringCount: Int,
    val categories: Map<String, Int>
)
