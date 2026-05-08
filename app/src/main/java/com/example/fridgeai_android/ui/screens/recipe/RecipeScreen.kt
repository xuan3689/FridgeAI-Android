package com.example.fridgeai_android.ui.screens.recipe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fridgeai_android.data.model.Recipe
import com.example.fridgeai_android.ui.components.BottomNavigationBar
import com.example.fridgeai_android.ui.navigation.Screen
import com.example.fridgeai_android.ui.theme.FridgeAmber
import com.example.fridgeai_android.ui.theme.FridgeBg
import com.example.fridgeai_android.ui.theme.FridgeBorder
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeInk3
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    navController: NavController,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.recommendedRecipes.isEmpty() && !uiState.isLoading) {
            viewModel.recommendRecipes()
        }
    }

    Scaffold(
        containerColor = FridgeBg,
        topBar = {
            TopAppBar(
                title = { Text("菜谱推荐", style = MaterialTheme.typography.headlineLarge) },
                actions = {
                    IconButton(onClick = { viewModel.recommendRecipes() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "刷新", tint = FridgeGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FridgeL0)
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController, currentRoute = Screen.Recipe.route) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(FridgeBg)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = FridgeGreen)
                uiState.error != null -> ErrorContent(message = uiState.error!!, onRetry = { viewModel.recommendRecipes() })
                uiState.recommendedRecipes.isEmpty() -> EmptyContent(onRefresh = { viewModel.recommendRecipes() })
                else -> RecipeList(recipes = uiState.recommendedRecipes, onRecipeClick = { viewModel.selectRecipe(it) })
            }
        }
    }

    uiState.selectedRecipe?.let { recipe ->
        RecipeDetailDialog(recipe = recipe, onDismiss = { viewModel.clearSelectedRecipe() })
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>, onRecipeClick: (Recipe) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(recipes) { recipe -> RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe) }) }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = FridgeL0),
        border = BorderStroke(0.5.dp, FridgeBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(recipe.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = FridgeGreen)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                RecipeInfoChip(Icons.Default.Timer, "${recipe.cookingTime}分钟")
                RecipeInfoChip(Icons.Default.Star, recipe.difficulty)
                RecipeInfoChip(Icons.Default.Category, recipe.category)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "所需食材：${recipe.ingredients.take(3).joinToString("、")}${if (recipe.ingredients.size > 3) "..." else ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = FridgeInk2
            )
        }
    }
}

@Composable
fun RecipeInfoChip(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = FridgeAmber)
        Text(text, style = MaterialTheme.typography.labelSmall, color = FridgeInk2)
    }
}

@Composable
fun EmptyContent(onRefresh: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(72.dp), tint = FridgeInk3)
        Spacer(modifier = Modifier.height(16.dp))
        Text("暂无菜谱推荐", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("添加食材后获取推荐", style = MaterialTheme.typography.bodyMedium, color = FridgeInk2)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRefresh,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FridgeGreen)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("刷新", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Error, contentDescription = null, modifier = Modifier.size(72.dp), tint = FridgeRed)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.bodyLarge, color = FridgeInk2)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FridgeGreen)
        ) { Text("重试", style = MaterialTheme.typography.labelLarge) }
    }
}
