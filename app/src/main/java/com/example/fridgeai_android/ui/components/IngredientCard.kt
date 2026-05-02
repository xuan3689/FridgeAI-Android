package com.example.fridgeai_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fridgeai_android.data.model.Ingredient
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IngredientCard(ingredient: Ingredient) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getCategoryColor(ingredient.category)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getCategoryEmoji(ingredient.category),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${ingredient.quantity}${ingredient.unit} · ${ingredient.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDate(ingredient.addedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            val remainingDays = ingredient.getRemainingDays()
            if (remainingDays <= 2) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (remainingDays <= 0) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = if (remainingDays <= 0) "已过期" else "${remainingDays}天",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (remainingDays <= 0)
                            MaterialTheme.colorScheme.onError
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun getCategoryColor(category: String): Color {
    return when (category) {
        "蔬菜" -> Color(0xFF4CAF50)
        "水果" -> Color(0xFFFF9800)
        "肉类" -> Color(0xFFF44336)
        "海鲜" -> Color(0xFF2196F3)
        "调料" -> Color(0xFF9C27B0)
        "主食" -> Color(0xFFFFEB3B)
        "蛋奶" -> Color(0xFFFFCDD2)
        else -> Color(0xFF9E9E9E)
    }
}

fun getCategoryEmoji(category: String): String {
    return when (category) {
        "蔬菜" -> "🥬"
        "水果" -> "🍎"
        "肉类" -> "🥩"
        "海鲜" -> "🐟"
        "调料" -> "🧂"
        "主食" -> "🍚"
        "蛋奶" -> "🥚"
        else -> "🍱"
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA)
    return sdf.format(Date(timestamp))
}
