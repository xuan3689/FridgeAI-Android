package com.example.fridgeai_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.fridgeai_android.ui.navigation.FridgeNavGraph
import com.example.fridgeai_android.ui.theme.FridgeAIAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FridgeAIAndroidTheme {
                val navController = rememberNavController()
                FridgeNavGraph(navController = navController)
            }
        }
    }
}