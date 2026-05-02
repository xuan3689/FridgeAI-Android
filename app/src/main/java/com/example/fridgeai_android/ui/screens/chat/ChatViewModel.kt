package com.example.fridgeai_android.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fridgeai_android.data.model.ChatMessage
import com.example.fridgeai_android.data.repository.ChatRepository
import com.example.fridgeai_android.data.repository.IngredientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    init {
        loadMessages()
    }
    
    private fun loadMessages() {
        viewModelScope.launch {
            chatRepository.getAllMessages().collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }
    
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)
            
            // 保存用户消息
            val userMessage = ChatMessage(role = "user", content = content)
            chatRepository.addMessage(userMessage)
            
            // 获取当前食材列表
            val ingredients = mutableListOf<String>()
            ingredientRepository.getAllIngredients().collect { list ->
                ingredients.addAll(list.map { it.name })
            }
            
            // 发送到后端
            val result = chatRepository.sendMessage(content, ingredients)
            
            if (result.isSuccess) {
                val reply = result.getOrNull() ?: "抱歉，我没有理解您的问题"
                val assistantMessage = ChatMessage(role = "assistant", content = reply)
                chatRepository.addMessage(assistantMessage)
            } else {
                // 降级到本地回复
                val fallbackReply = generateFallbackReply(content, ingredients)
                val assistantMessage = ChatMessage(role = "assistant", content = fallbackReply)
                chatRepository.addMessage(assistantMessage)
            }
            
            _uiState.value = _uiState.value.copy(isSending = false)
        }
    }
    
    fun startVoiceRecording() {
        _uiState.value = _uiState.value.copy(isRecording = true)
    }
    
    fun stopVoiceRecording() {
        _uiState.value = _uiState.value.copy(isRecording = false)
        // TODO: 实现语音识别
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            chatRepository.clearHistory()
        }
    }
    
    private fun generateFallbackReply(message: String, ingredients: List<String>): String {
        return when {
            message.contains("推荐", ignoreCase = true) || message.contains("做什么", ignoreCase = true) -> {
                if (ingredients.isEmpty()) {
                    "您的冰箱里还没有食材呢，快去添加一些吧！"
                } else {
                    "根据您冰箱里的${ingredients.take(3).joinToString("、")}，我建议您可以做一道简单的家常菜。"
                }
            }
            message.contains("过期", ignoreCase = true) -> {
                "建议您定期检查食材的保质期，及时使用快过期的食材。"
            }
            message.contains("保存", ignoreCase = true) || message.contains("储存", ignoreCase = true) -> {
                "不同食材有不同的储存方法，蔬菜建议冷藏，肉类建议冷冻保存。"
            }
            else -> {
                "我是冰小智，您的智能冰箱助手。您可以问我关于食材管理、菜谱推荐等问题。"
            }
        }
    }
}

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val isRecording: Boolean = false
)
