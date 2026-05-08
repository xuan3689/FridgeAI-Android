package com.example.fridgeai_android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import com.example.fridgeai_android.ui.navigation.Screen
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeGreenL
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeInk3
import com.example.fridgeai_android.ui.theme.FridgeL0

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = FridgeGreen,
        selectedTextColor = FridgeGreen,
        indicatorColor = FridgeGreenL,
        unselectedIconColor = FridgeInk3,
        unselectedTextColor = FridgeInk2
    )

    NavigationBar(
        containerColor = FridgeL0,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
            label = { Text("首页", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == Screen.Home.route,
            colors = itemColors,
            onClick = {
                if (currentRoute != Screen.Home.route) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Inventory, contentDescription = "库存") },
            label = { Text("库存", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == Screen.Inventory.route,
            colors = itemColors,
            onClick = {
                if (currentRoute != Screen.Inventory.route) {
                    navController.navigate(Screen.Inventory.route)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CameraAlt, contentDescription = "识别") },
            label = { Text("识别", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == Screen.Camera.route,
            colors = itemColors,
            onClick = {
                if (currentRoute != Screen.Camera.route) {
                    navController.navigate(Screen.Camera.route)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "菜谱") },
            label = { Text("菜谱", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == Screen.Recipe.route,
            colors = itemColors,
            onClick = {
                if (currentRoute != Screen.Recipe.route) {
                    navController.navigate(Screen.Recipe.route)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "助手") },
            label = { Text("助手", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == Screen.Chat.route,
            colors = itemColors,
            onClick = {
                if (currentRoute != Screen.Chat.route) {
                    navController.navigate(Screen.Chat.route)
                }
            }
        )
    }
}
