package com.example.fridgeai_android.ui.screens.expiring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fridgeai_android.ui.components.IngredientCard
import com.example.fridgeai_android.ui.theme.FridgeAmber
import com.example.fridgeai_android.ui.theme.FridgeBg
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeInk3
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiringScreen(
    navController: NavController,
    viewModel: ExpiringViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = FridgeBg,
        topBar = {
            TopAppBar(
                title = { Text("临期提醒", style = MaterialTheme.typography.headlineLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = FridgeInk2)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FridgeL0)
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(FridgeBg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FridgeGreen)
            }
        } else if (uiState.expiredIngredients.isEmpty() && uiState.expiringSoonIngredients.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(FridgeBg),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = FridgeGreen
                    )
                    Text("太棒了！", style = MaterialTheme.typography.headlineSmall)
                    Text("暂无临期或过期食材", style = MaterialTheme.typography.bodyMedium, color = FridgeInk2)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(FridgeBg),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (uiState.expiredIngredients.isNotEmpty()) {
                    item {
                        Text(
                            text = "EXPIRED · ${uiState.expiredIngredients.size}",
                            style = MaterialTheme.typography.titleSmall,
                            color = FridgeRed
                        )
                    }
                    items(uiState.expiredIngredients) { ingredient -> IngredientCard(ingredient = ingredient) }
                }

                if (uiState.expiringSoonIngredients.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "EXPIRING SOON · ${uiState.expiringSoonIngredients.size}",
                            style = MaterialTheme.typography.titleSmall,
                            color = FridgeAmber
                        )
                    }
                    items(uiState.expiringSoonIngredients) { ingredient -> IngredientCard(ingredient = ingredient) }
                }
            }
        }
    }
}
