package com.example.fridgeai_android.ui.screens.recipe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fridgeai_android.data.model.Recipe
import com.example.fridgeai_android.ui.theme.FridgeBorder
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeGreenL
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeL1

@Composable
fun RecipeDetailDialog(
    recipe: Recipe,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = FridgeL0),
            border = BorderStroke(0.5.dp, FridgeBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "关闭", tint = FridgeInk2)
                    }
                }

                Divider(color = FridgeBorder)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            InfoChip(Icons.Default.Timer, "烹饪时间", "${recipe.cookingTime}分钟")
                            InfoChip(Icons.Default.Star, "难度", recipe.difficulty)
                        }
                    }

                    item {
                        Text("INGREDIENTS", style = MaterialTheme.typography.titleSmall, color = FridgeInk2)
                    }

                    itemsIndexed(recipe.ingredients) { index, ingredient ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(shape = RoundedCornerShape(4.dp), color = FridgeGreenL) {
                                Text(
                                    text = "${index + 1}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FridgeGreen
                                )
                            }
                            Text(ingredient, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    item {
                        Text("STEPS", style = MaterialTheme.typography.titleSmall, color = FridgeInk2)
                    }

                    itemsIndexed(recipe.steps) { index, step ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = FridgeL1),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(0.5.dp, FridgeBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(shape = RoundedCornerShape(6.dp), color = FridgeGreen) {
                                    Text(
                                        text = "${index + 1}",
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = FridgeL0
                                    )
                                }
                                Text(step, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(icon: ImageVector, label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FridgeGreenL),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, FridgeBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = FridgeGreen)
            Column {
                Text(label, style = MaterialTheme.typography.labelSmall, color = FridgeInk2)
                Text(value, style = MaterialTheme.typography.bodyMedium, color = FridgeGreen)
            }
        }
    }
}
