package com.example.fridgeai_android.ui.screens.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fridgeai_android.data.model.Ingredient
import com.example.fridgeai_android.ui.components.BottomNavigationBar
import com.example.fridgeai_android.ui.navigation.Screen
import com.example.fridgeai_android.ui.theme.FridgeBg
import com.example.fridgeai_android.ui.theme.FridgeBorder
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeGreenL
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeInk3
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeL1
import com.example.fridgeai_android.ui.theme.FridgeL2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(navController: NavController, viewModel: CameraViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { hasCameraPermission = it }

    LaunchedEffect(Unit) { if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA) }

    Scaffold(
        containerColor = FridgeBg,
        topBar = {
            TopAppBar(
                title = { Text("食材识别", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Default.ArrowBack, "返回", tint = FridgeInk2) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FridgeL0)
            )
        },
        bottomBar = { BottomNavigationBar(navController, Screen.Camera.route) }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues).background(FridgeBg)) {
            if (!hasCameraPermission) PermissionDeniedContent { launcher.launch(Manifest.permission.CAMERA) } else CameraPreviewContent(viewModel, uiState)
        }
    }

    if (uiState.showConfirmDialog && uiState.recognizedName != null) {
        RecognitionResultDialog(
            name = uiState.recognizedName!!,
            confidence = uiState.confidence,
            category = uiState.category ?: "其他",
            onDismiss = { viewModel.dismissDialog() },
            onConfirm = { viewModel.saveIngredient(it) }
        )
    }
}

@Composable
fun PermissionDeniedContent(onRequestPermission: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(72.dp), tint = FridgeGreen)
        Spacer(Modifier.height(24.dp))
        Text("需要相机权限", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("请授予相机权限以识别食材", style = MaterialTheme.typography.bodyMedium, color = FridgeInk2)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequestPermission, shape = RoundedCornerShape(6.dp), colors = ButtonDefaults.buttonColors(containerColor = FridgeGreen)) {
            Text("授予权限", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun CameraPreviewContent(viewModel: CameraViewModel, uiState: CameraUiState) {
    Box(Modifier.fillMaxSize().padding(20.dp)) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = FridgeL1),
            border = BorderStroke(0.5.dp, FridgeBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(112.dp), tint = FridgeInk3)
                Spacer(Modifier.height(16.dp))
                Text("相机预览区域", style = MaterialTheme.typography.titleLarge)
                Text("将食材放在镜头前进行识别", style = MaterialTheme.typography.bodyMedium, color = FridgeInk2)
            }
        }

        Card(
            modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = FridgeL0),
            border = BorderStroke(0.5.dp, FridgeBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.Info, null, tint = FridgeGreen)
                Text("将食材对准镜头，点击拍照识别", style = MaterialTheme.typography.bodyMedium, color = FridgeInk2)
            }
        }

        Column(Modifier.align(Alignment.BottomCenter).padding(bottom = 36.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
            if (uiState.isProcessing) {
                CircularProgressIndicator(color = FridgeGreen)
                Text("识别中...", color = FridgeInk2, style = MaterialTheme.typography.bodyMedium)
            } else {
                FloatingActionButton(
                    onClick = {
                        viewModel.setProcessing(true)
                        viewModel.onRecognitionResult("番茄", 0.92f, "蔬菜")
                        viewModel.setProcessing(false)
                    },
                    modifier = Modifier.size(72.dp),
                    containerColor = FridgeGreen,
                    contentColor = FridgeL0,
                    elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) { Icon(Icons.Default.CameraAlt, "拍照识别", modifier = Modifier.size(34.dp)) }
                Text("点击拍照", color = FridgeInk2, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun RecognitionResultDialog(name: String, confidence: Float, category: String, onDismiss: () -> Unit, onConfirm: (Ingredient) -> Unit) {
    var editedName by remember { mutableStateOf(name) }
    var quantity by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("个") }
    var editedCategory by remember { mutableStateOf(category) }
    var expireDays by remember { mutableStateOf("7") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FridgeL0,
        shape = RoundedCornerShape(10.dp),
        icon = { Icon(Icons.Default.CheckCircle, null, tint = FridgeGreen, modifier = Modifier.size(44.dp)) },
        title = { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("识别成功", style = MaterialTheme.typography.headlineSmall); Text("置信度: ${(confidence * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = FridgeInk2) } },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                CameraDialogField(editedName, { editedName = it }, "食材名称")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CameraDialogField(quantity, { quantity = it }, "数量", Modifier.weight(1f))
                    CameraDialogField(unit, { unit = it }, "单位", Modifier.weight(1f))
                }
                CameraDialogField(editedCategory, { editedCategory = it }, "分类")
                CameraDialogField(expireDays, { expireDays = it }, "保质期(天)")
            }
        },
        confirmButton = { Button(onClick = { onConfirm(Ingredient(name = editedName, quantity = quantity.toFloatOrNull() ?: 1f, unit = unit, category = editedCategory, expireDays = expireDays.toIntOrNull() ?: 7, source = "camera")) }, shape = RoundedCornerShape(6.dp), colors = ButtonDefaults.buttonColors(containerColor = FridgeGreen)) { Text("保存", style = MaterialTheme.typography.labelLarge) } },
        dismissButton = { TextButton(onClick = onDismiss, colors = ButtonDefaults.textButtonColors(contentColor = FridgeInk2)) { Text("取消", style = MaterialTheme.typography.labelLarge) } }
    )
}

@Composable
private fun CameraDialogField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier.fillMaxWidth()) {
    OutlinedTextField(value, onValueChange, modifier = modifier, label = { Text(label, style = MaterialTheme.typography.labelSmall) }, singleLine = true, shape = RoundedCornerShape(6.dp), colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = FridgeL2, unfocusedContainerColor = FridgeL2, focusedBorderColor = FridgeGreen, unfocusedBorderColor = FridgeBorder, cursorColor = FridgeGreen))
}
