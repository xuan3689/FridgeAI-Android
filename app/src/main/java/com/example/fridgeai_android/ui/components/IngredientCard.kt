package com.example.fridgeai_android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fridgeai_android.data.model.Ingredient
import com.example.fridgeai_android.ui.theme.FridgeAmber
import com.example.fridgeai_android.ui.theme.FridgeAmberL
import com.example.fridgeai_android.ui.theme.FridgeBorder
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeGreenL
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeL1
import com.example.fridgeai_android.ui.theme.FridgeRed
import com.example.fridgeai_android.ui.theme.FridgeRedL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IngredientCard(ingredient: Ingredient) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = FridgeL0),
        border = BorderStroke(0.5.dp, FridgeBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(getCategoryColor(ingredient.category)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getCategoryEmoji(ingredient.category),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${ingredient.quantity}${ingredient.unit} · ${ingredient.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = FridgeInk2
                )
                Text(
                    text = formatDate(ingredient.addedAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val remainingDays = ingredient.getRemainingDays()
            if (remainingDays <= 2) {
                val expired = remainingDays <= 0
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (expired) FridgeRedL else FridgeAmberL,
                    border = BorderStroke(0.5.dp, if (expired) FridgeRed.copy(alpha = 0.28f) else FridgeAmber.copy(alpha = 0.28f))
                ) {
                    Text(
                        text = if (expired) "已过期" else "${remainingDays}天",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (expired) FridgeRed else FridgeAmber
                    )
                }
            }
        }
    }
}

@Composable
fun getCategoryColor(category: String): Color {
    return when (category) {
        "蔬菜" -> FridgeGreenL
        "水果" -> FridgeAmberL
        "肉类" -> FridgeRedL
        "海鲜" -> FridgeL1
        "调料" -> FridgeL1
        "主食" -> FridgeAmberL.copy(alpha = 0.65f)
        "蛋奶" -> FridgeRedL.copy(alpha = 0.62f)
        else -> FridgeGreenL.copy(alpha = 0.55f)
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
