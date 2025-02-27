package kg.geeks.rickandmortywithjetpackcompose.ui.screens.character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import kg.geeks.rickandmortywithjetpackcompose.data.local.FavoriteCharacterEntity
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.favorite.FavoriteViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterScreen(
    navController: NavHostController,
    viewModel: CharactersViewModel = koinViewModel(),
    favoriteViewModel: FavoriteViewModel
) {
    val characters = viewModel.charactersPager.collectAsLazyPagingItems()
    val filters = viewModel.filters.value
    var showFilterDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(filters.name ?: "") }
    var tempStatus by remember { mutableStateOf(filters.status) }
    var tempSpecies by remember { mutableStateOf(filters.species) }
    var tempGender by remember { mutableStateOf(filters.gender) }

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

        // Кнопки для фильтров и сброса
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
                tempStatus = null
                tempSpecies = null
                tempGender = null
                characters.refresh()
            }) {
                Text("Reset")
            }
        }

        // Список персонажей
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(count = characters.itemCount) { index ->
                val character = characters[index]
                character?.let {
                    CharacterItem(
                        character = it,
                        onClick = { navController.navigate("character_detail/${it.id}") },
                        onLongClick = { favoriteViewModel.addCharacterToFavorites(it) }
                    )
                }
            }
            // Обработка состояний загрузки
            characters.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    loadState.refresh is LoadState.Error -> item {
                        val error = (loadState.refresh as LoadState.Error).error
                        Text(text = "Error: ${error.localizedMessage}", modifier = Modifier.padding(16.dp))
                    }
                    loadState.append is LoadState.Loading -> item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
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
            title = { Text("Filter Characters") },
            text = {
                Column {
                    // Status
                    Text("Status", style = MaterialTheme.typography.titleMedium)
                    listOf("Alive", "Dead", "Unknown").forEach { status ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tempStatus == status.lowercase(),
                                onClick = { tempStatus = status.lowercase() }
                            )
                            Text(status)
                        }
                    }

                    // Species (пример, можно расширить)
                    Text("Species", style = MaterialTheme.typography.titleMedium)
                    listOf("Human", "Alien").forEach { species ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tempSpecies == species.lowercase(),
                                onClick = { tempSpecies = species.lowercase() }
                            )
                            Text(species)
                        }
                    }

                    // Gender
                    Text("Gender", style = MaterialTheme.typography.titleMedium)
                    listOf("Male", "Female", "Genderless", "Unknown").forEach { gender ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tempGender == gender.lowercase(),
                                onClick = { tempGender = gender.lowercase() }
                            )
                            Text(gender)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateFilters(
                        name = tempName.takeIf { it.isNotBlank() },
                        status = tempStatus,
                        species = tempSpecies,
                        gender = tempGender
                    )
                    showFilterDialog = false
                    characters.refresh()
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterItem(
    character: FavoriteCharacterEntity,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = rememberAsyncImagePainter(character.image)
        Image(painter = painter, contentDescription = null, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = character.name, style = MaterialTheme.typography.titleLarge)
            Text(text = character.species, style = MaterialTheme.typography.titleMedium)
        }
    }
}