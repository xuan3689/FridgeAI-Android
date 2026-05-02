package com.example.fridgeai_android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.fridgeai_android.ui.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
            label = { Text("首页") },
            selected = currentRoute == Screen.Home.route,
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
            label = { Text("库存") },
            selected = currentRoute == Screen.Inventory.route,
            onClick = { 
                if (currentRoute != Screen.Inventory.route) {
                    navController.navigate(Screen.Inventory.route)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CameraAlt, contentDescription = "识别") },
            label = { Text("识别") },
            selected = currentRoute == Screen.Camera.route,
            onClick = { 
                if (currentRoute != Screen.Camera.route) {
                    navController.navigate(Screen.Camera.route)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "菜谱") },
            label = { Text("菜谱") },
            selected = currentRoute == Screen.Recipe.route,
            onClick = { 
                if (currentRoute != Screen.Recipe.route) {
                    navController.navigate(Screen.Recipe.route)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "助手") },
            label = { Text("助手") },
            selected = currentRoute == Screen.Chat.route,
            onClick = { 
                if (currentRoute != Screen.Chat.route) {
                    navController.navigate(Screen.Chat.route)
                }
            }
        )
    }
}
