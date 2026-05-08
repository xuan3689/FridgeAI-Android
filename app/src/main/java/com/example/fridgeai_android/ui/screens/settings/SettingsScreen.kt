package com.example.fridgeai_android.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fridgeai_android.ui.theme.FridgeBg
import com.example.fridgeai_android.ui.theme.FridgeBorder
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeGreenL
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeInk3
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeL2

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = FridgeBg,
        topBar = {
            TopAppBar(
                title = { Text("设置", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Default.ArrowBack, "返回", tint = FridgeInk2) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FridgeL0)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(FridgeBg),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { SectionTitle("DEVICE") }
            item { SettingItem(Icons.Default.Wifi, "RK3588 设备", "192.168.1.100:8000") { } }
            item { SettingItem(Icons.Default.Bluetooth, "蓝牙连接", "未连接") { } }
            item { Divider(color = FridgeBorder, modifier = Modifier.padding(vertical = 8.dp)) }
            item { SectionTitle("NOTIFICATIONS") }
            item { var enabled by remember { mutableStateOf(true) }; SettingSwitchItem(Icons.Default.Notifications, "临期提醒", "食材即将过期时通知", enabled) { enabled = it } }
            item { var enabled by remember { mutableStateOf(true) }; SettingSwitchItem(Icons.Default.VolumeUp, "语音播报", "开启语音反馈", enabled) { enabled = it } }
            item { Divider(color = FridgeBorder, modifier = Modifier.padding(vertical = 8.dp)) }
            item { SectionTitle("DATA") }
            item { SettingItem(Icons.Default.CloudUpload, "数据同步", "同步到云端") { } }
            item { SettingItem(Icons.Default.Delete, "清除缓存", "清除本地缓存数据") { } }
            item { Divider(color = FridgeBorder, modifier = Modifier.padding(vertical = 8.dp)) }
            item { SectionTitle("ABOUT") }
            item { SettingItem(Icons.Default.Info, "关于冰小智", "版本 1.0.0") { showAboutDialog = true } }
            item { SettingItem(Icons.Default.Help, "帮助与反馈", "使用帮助和问题反馈") { } }
        }
    }

    if (showAboutDialog) AboutDialog { showAboutDialog = false }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall, color = FridgeInk2, modifier = Modifier.padding(top = 6.dp, bottom = 2.dp))
}

@Composable
fun SettingItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = FridgeL0),
        border = BorderStroke(0.5.dp, FridgeBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = FridgeGreen, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = FridgeInk2)
            }
            Icon(Icons.Default.ChevronRight, null, tint = FridgeInk3)
        }
    }
}

@Composable
fun SettingSwitchItem(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = FridgeL0),
        border = BorderStroke(0.5.dp, FridgeBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = FridgeGreen, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = FridgeInk2)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = FridgeL0, checkedTrackColor = FridgeGreen, uncheckedThumbColor = FridgeInk3, uncheckedTrackColor = FridgeL2, uncheckedBorderColor = FridgeBorder)
            )
        }
    }
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FridgeL0,
        shape = RoundedCornerShape(10.dp),
        icon = { Text("冰", style = MaterialTheme.typography.headlineMedium, color = FridgeGreen) },
        title = { Text("冰小智", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("智能冰箱家庭助手", style = MaterialTheme.typography.titleMedium)
                Text("版本：1.0.0", style = MaterialTheme.typography.bodyMedium, color = FridgeInk2)
                Text("基于 RK3588 开发板的外挂式 AI 冰箱助手，端云混合架构，摄像头识别食材 + 语音交互 + 菜谱推荐。", style = MaterialTheme.typography.bodySmall, color = FridgeInk2)
                Text("© 2024 FridgeAI Team", style = MaterialTheme.typography.bodySmall, color = FridgeInk2)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss, colors = ButtonDefaults.textButtonColors(contentColor = FridgeGreen)) { Text("确定", style = MaterialTheme.typography.labelLarge) } }
    )
}
