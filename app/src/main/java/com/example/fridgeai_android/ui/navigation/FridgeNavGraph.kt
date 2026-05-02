package com.example.fridgeai_android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fridgeai_android.ui.screens.camera.CameraScreen
import com.example.fridgeai_android.ui.screens.chat.ChatScreen
import com.example.fridgeai_android.ui.screens.expiring.ExpiringScreen
import com.example.fridgeai_android.ui.screens.home.HomeScreen
import com.example.fridgeai_android.ui.screens.inventory.InventoryScreen
import com.example.fridgeai_android.ui.screens.recipe.RecipeScreen
import com.example.fridgeai_android.ui.screens.settings.SettingsScreen

@Composable
fun FridgeNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Inventory.route) {
            InventoryScreen(navController = navController)
        }
        
        composable(Screen.Camera.route) {
            CameraScreen(navController = navController)
        }
        
        composable(Screen.Recipe.route) {
            RecipeScreen(navController = navController)
        }
        
        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }
        
        composable(Screen.Expiring.route) {
            ExpiringScreen(navController = navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}
