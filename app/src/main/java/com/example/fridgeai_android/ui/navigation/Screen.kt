package com.example.fridgeai_android.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Inventory : Screen("inventory")
    object Camera : Screen("camera")
    object Recipe : Screen("recipe")
    object Chat : Screen("chat")
    object Expiring : Screen("expiring")
    object Settings : Screen("settings")
    object IngredientDetail : Screen("ingredient/{ingredientId}") {
        fun createRoute(ingredientId: Int) = "ingredient/$ingredientId"
    }
    object RecipeDetail : Screen("recipe/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe/$recipeId"
    }
}
