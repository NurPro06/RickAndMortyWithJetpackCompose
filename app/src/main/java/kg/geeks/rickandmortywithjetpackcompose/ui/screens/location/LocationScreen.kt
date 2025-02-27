package kg.geeks.rickandmortywithjetpackcompose.ui.screens.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import kg.geeks.rickandmortywithjetpackcompose.data.dto.location.LocationResponseDto
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationScreen(
    navController: NavHostController,
    viewModel: LocationViewModel = koinViewModel()
) {
    val locations = viewModel.locationPager.collectAsLazyPagingItems()
    val filters = viewModel.filters.value
    var showFilterDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(filters.name ?: "") }
    var tempType by remember { mutableStateOf(filters.type) }
    var tempDimension by remember { mutableStateOf(filters.dimension) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Поле поиска по имени
        TextField(
            value = tempName,
            onValueChange = { tempName = it },
            label = { Text("Search by name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Кнопки фильтров и сброса
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showFilterDialog = true }) {
                Text("Filters")
            }
            Button(onClick = {
                viewModel.resetFilters()
                tempName = ""
                tempType = null
                tempDimension = null
                locations.refresh()
            }) {
                Text("Reset")
            }
        }

        // Список локаций
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(count = locations.itemCount) { index ->
                val location = locations[index]
                location?.let {
                    LocationItem(location = it) {
                        navController.navigate("location_detail/${it.id}")
                    }
                }
            }

            locations.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        item {
                            Text(
                                text = "Ошибка загрузки: ${e.error.localizedMessage}",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Диалог фильтров
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Locations") },
            text = {
                Column {
                    Text("Type", style = MaterialTheme.typography.titleMedium)
                    listOf("Planet", "Space Station", "Microverse").forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tempType == type.lowercase(),
                                onClick = { tempType = type.lowercase() }
                            )
                            Text(type)
                        }
                    }
                    Text("Dimension", style = MaterialTheme.typography.titleMedium)
                    listOf("Dimension C-137", "Unknown", "Replacement Dimension").forEach { dim ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tempDimension == dim.lowercase(),
                                onClick = { tempDimension = dim.lowercase() }
                            )
                            Text(dim)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateFilters(
                        name = tempName.takeIf { it.isNotBlank() },
                        type = tempType,
                        dimension = tempDimension
                    )
                    showFilterDialog = false
                    locations.refresh()
                }) {
                    Text("Apply")
                }
            },
            dismissButton = {
                Button(onClick = { showFilterDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun LocationItem(location: LocationResponseDto.Location, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = rememberAsyncImagePainter(model = location.imageUrl)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = location.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}