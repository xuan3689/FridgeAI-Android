package com.example.fridgeai_android.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.fridgeai_android.ui.components.BottomNavigationBar
import com.example.fridgeai_android.ui.components.IngredientCard
import com.example.fridgeai_android.ui.navigation.Screen
import com.example.fridgeai_android.ui.theme.FridgeBg
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeL0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = FridgeBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "冰小智",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "SMART FRIDGE COMPANION",
                            style = MaterialTheme.typography.titleSmall,
                            color = FridgeInk2
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "设置", tint = FridgeGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FridgeL0)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = Screen.Home.route
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(FridgeBg),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { QuickActionsCard(navController) }
                item { StatsCard(uiState) }

                if (uiState.expiringCount > 0) {
                    item {
                        ExpiringWarningCard(
                            count = uiState.expiringCount,
                            onClick = { navController.navigate(Screen.Expiring.route) }
                        )
                    }
                }

                item {
                    Text(
                        text = "RECENTLY ADDED",
                        style = MaterialTheme.typography.titleSmall,
                        color = FridgeInk2,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                items(uiState.recentIngredients) { ingredient ->
                    IngredientCard(ingredient = ingredient)
                }

                if (uiState.recentIngredients.isNotEmpty()) {
                    item {
                        TextButton(
                            onClick = { navController.navigate(Screen.Inventory.route) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("查看全部食材", style = MaterialTheme.typography.labelLarge, color = FridgeGreen)
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = FridgeGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
