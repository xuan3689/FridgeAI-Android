package com.example.fridgeai_android.ui.screens.inventory

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fridgeai_android.data.model.Ingredient
import com.example.fridgeai_android.ui.components.BottomNavigationBar
import com.example.fridgeai_android.ui.components.IngredientCard
import com.example.fridgeai_android.ui.navigation.Screen
import com.example.fridgeai_android.ui.theme.FridgeBg
import com.example.fridgeai_android.ui.theme.FridgeBorder
import com.example.fridgeai_android.ui.theme.FridgeGreen
import com.example.fridgeai_android.ui.theme.FridgeGreenL
import com.example.fridgeai_android.ui.theme.FridgeInk2
import com.example.fridgeai_android.ui.theme.FridgeInk3
import com.example.fridgeai_android.ui.theme.FridgeL0
import com.example.fridgeai_android.ui.theme.FridgeL2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = FridgeBg,
        topBar = {
            TopAppBar(
                title = { Text("食材库存", style = MaterialTheme.typography.headlineLarge) },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "添加食材", tint = FridgeGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FridgeL0)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = Screen.Inventory.route)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(FridgeBg)
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            CategoryFilter(
                categories = uiState.categorizedIngredients.keys.toList(),
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.setSelectedCategory(it) },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = FridgeGreen)
                }
            } else if (uiState.filteredIngredients.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = FridgeInk3
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("暂无食材", style = MaterialTheme.typography.titleMedium, color = FridgeInk2)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredIngredients) { ingredient ->
                        IngredientCard(ingredient = ingredient)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddIngredientDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { ingredient ->
                viewModel.updateIngredient(ingredient)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("搜索食材...", style = MaterialTheme.typography.bodyMedium) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = FridgeInk2) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "清除", tint = FridgeInk2)
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(6.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FridgeL2,
            unfocusedContainerColor = FridgeL2,
            focusedBorderColor = FridgeGreen,
            unfocusedBorderColor = FridgeBorder,
            cursorColor = FridgeGreen,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun CategoryFilter(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            EarthFilterChip(selected = selectedCategory == null, onClick = { onCategorySelected(null) }, label = "全部")
        }
        items(categories) { category ->
            EarthFilterChip(selected = selectedCategory == category, onClick = { onCategorySelected(category) }, label = category)
        }
    }
}

@Composable
private fun EarthFilterChip(selected: Boolean, onClick: () -> Unit, label: String) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        shape = RoundedCornerShape(4.dp),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = FridgeBorder,
            selectedBorderColor = FridgeGreen
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = FridgeL0,
            labelColor = FridgeInk2,
            selectedContainerColor = FridgeGreenL,
            selectedLabelColor = FridgeGreen
        )
    )
}

@Composable
fun AddIngredientDialog(
    onDismiss: () -> Unit,
    onConfirm: (Ingredient) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("个") }
    var category by remember { mutableStateOf("其他") }
    var expireDays by remember { mutableStateOf("7") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FridgeL0,
        shape = RoundedCornerShape(10.dp),
        title = { Text("添加食材", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DialogField(name, { name = it }, "食材名称")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DialogField(quantity, { quantity = it }, "数量", Modifier.weight(1f))
                    DialogField(unit, { unit = it }, "单位", Modifier.weight(1f))
                }
                DialogField(category, { category = it }, "分类")
                DialogField(expireDays, { expireDays = it }, "保质期(天)")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            Ingredient(
                                name = name,
                                quantity = quantity.toFloatOrNull() ?: 1f,
                                unit = unit,
                                category = category,
                                expireDays = expireDays.toIntOrNull() ?: 7,
                                source = "manual"
                            )
                        )
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor = FridgeGreen)
            ) { Text("确定", style = MaterialTheme.typography.labelLarge) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, colors = ButtonDefaults.textButtonColors(contentColor = FridgeInk2)) {
                Text("取消", style = MaterialTheme.typography.labelLarge)
            }
        }
    )
}

@Composable
private fun DialogField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(6.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FridgeL2,
            unfocusedContainerColor = FridgeL2,
            focusedBorderColor = FridgeGreen,
            unfocusedBorderColor = FridgeBorder,
            cursorColor = FridgeGreen
        )
    )
}
