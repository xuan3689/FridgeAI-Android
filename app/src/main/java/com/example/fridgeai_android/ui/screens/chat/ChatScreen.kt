package com.example.fridgeai_android.ui.screens.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fridgeai_android.data.model.ChatMessage
import com.example.fridgeai_android.ui.components.BottomNavigationBar
import com.example.fridgeai_android.ui.navigation.Screen
import com.example.fridgeai_android.ui.theme.FridgeBg
import com.example.fridgeai_android.ui.theme.FridgeBorder
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeGreenL
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeL1
import com.example.fridgeai_android.ui.theme.FridgeL2
import com.example.fridgeai_android.ui.theme.FridgeRed
import com.example.fridgeai_android.ui.theme.FridgeRedL
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: ChatViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) coroutineScope.launch { listState.animateScrollToItem(uiState.messages.size - 1) }
    }

    Scaffold(
        containerColor = FridgeBg,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.size(40.dp).clip(CircleShape).background(FridgeGreenL), contentAlignment = Alignment.Center) { Text("🤖", style = MaterialTheme.typography.titleLarge) }
                        Column {
                            Text("冰小智", style = MaterialTheme.typography.headlineSmall)
                            Text(if (uiState.isSending) "正在输入..." else "ONLINE", style = MaterialTheme.typography.labelSmall, color = FridgeInk2)
                        }
                    }
                },
                actions = { IconButton(onClick = { viewModel.clearHistory() }) { Icon(Icons.Default.Delete, "清空历史", tint = FridgeInk2) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FridgeL0)
            )
        },
        bottomBar = {
            Column {
                ChatInputBar(inputText, { inputText = it }, { viewModel.sendMessage(inputText); inputText = "" }, { if (uiState.isRecording) viewModel.stopVoiceRecording() else viewModel.startVoiceRecording() }, uiState.isRecording, uiState.isSending)
                BottomNavigationBar(navController, Screen.Chat.route)
            }
        }
    ) { paddingValues ->
        if (uiState.messages.isEmpty()) {
            EmptyChatContent(Modifier.fillMaxSize().padding(paddingValues).background(FridgeBg))
        } else {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(paddingValues).background(FridgeBg), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.messages) { ChatMessageItem(it) }
            }
        }
    }
}

@Composable
fun EmptyChatContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("你好！我是冰小智", style = MaterialTheme.typography.headlineSmall)
        Text("您可以问我关于食材管理、菜谱推荐等问题", style = MaterialTheme.typography.bodyMedium, color = FridgeInk2, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SuggestionChip("今天吃什么？")
            SuggestionChip("推荐一道菜")
            SuggestionChip("哪些食材快过期了？")
        }
    }
}

@Composable
fun SuggestionChip(text: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = FridgeL0, border = BorderStroke(0.5.dp, FridgeBorder), modifier = Modifier.clickable { }) {
        Text(text, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.bodyMedium, color = FridgeInk2)
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val isUser = message.role == "user"
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start) {
        if (!isUser) {
            Box(Modifier.size(32.dp).clip(CircleShape).background(FridgeGreenL), contentAlignment = Alignment.Center) { Text("🤖", style = MaterialTheme.typography.bodyMedium) }
            Spacer(Modifier.width(8.dp))
        }
        Column(modifier = Modifier.widthIn(max = 280.dp), horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
            Surface(shape = RoundedCornerShape(10.dp), color = if (isUser) FridgeGreen else FridgeL0, border = BorderStroke(0.5.dp, if (isUser) FridgeGreen else FridgeBorder)) {
                Text(message.content, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium, color = if (isUser) FridgeL0 else FridgeInk2)
            }
            Text(formatTime(message.createdAt), style = MaterialTheme.typography.labelSmall, color = FridgeInk2, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
        }
        if (isUser) {
            Spacer(Modifier.width(8.dp))
            Box(Modifier.size(32.dp).clip(CircleShape).background(FridgeL1), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, modifier = Modifier.size(18.dp), tint = FridgeInk2) }
        }
    }
}

@Composable
fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, onVoiceClick: () -> Unit, isRecording: Boolean, isSending: Boolean) {
    Surface(color = FridgeL0, tonalElevation = 0.dp, border = BorderStroke(0.5.dp, FridgeBorder)) {
        Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = onVoiceClick, colors = IconButtonDefaults.iconButtonColors(containerColor = if (isRecording) FridgeRedL else FridgeL2)) {
                Icon(if (isRecording) Icons.Default.Stop else Icons.Default.Mic, if (isRecording) "停止录音" else "语音输入", tint = if (isRecording) FridgeRed else FridgeInk2)
            }
            OutlinedTextField(value = text, onValueChange = onTextChange, modifier = Modifier.weight(1f), placeholder = { Text("输入消息...", style = MaterialTheme.typography.bodyMedium) }, maxLines = 4, enabled = !isSending, shape = RoundedCornerShape(6.dp), colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = FridgeL2, unfocusedContainerColor = FridgeL2, focusedBorderColor = FridgeGreen, unfocusedBorderColor = FridgeBorder, cursorColor = FridgeGreen))
            IconButton(onClick = onSend, enabled = text.isNotBlank() && !isSending, colors = IconButtonDefaults.iconButtonColors(containerColor = FridgeGreen, disabledContainerColor = FridgeL2)) {
                if (isSending) CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp, color = FridgeGreen) else Icon(Icons.Default.Send, "发送", tint = if (text.isNotBlank()) FridgeL0 else FridgeInk2)
            }
        }
    }
}

fun formatTime(timestamp: Long): String = SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(timestamp))
